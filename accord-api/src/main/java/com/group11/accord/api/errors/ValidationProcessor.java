package com.group11.accord.api.errors;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ElementKind;
import jakarta.validation.Path;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.method.ParameterErrors;
import org.springframework.validation.method.ParameterValidationResult;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@NoArgsConstructor(access = AccessLevel.NONE)
public final class ValidationProcessor {

    private static final String ELEMENT_ACCESS_TEMPLATE = "%s[%s]";

    /**
     * Add errors for the given {@link ConstraintViolation}s to the provided {@link ValidationDetail}.
     *
     * @param violations the {@link ConstraintViolation}s to process
     * @param detail the {@link ValidationDetail} to add the errors to
     */
    public static void addConstraintViolationsToValidationDetail(Collection<ConstraintViolation<?>> violations, ValidationDetail detail) {
        for (ConstraintViolation<?> violation : violations) {
            detail.addError(violation.getMessage(), getViolationPath(violation.getPropertyPath()));
        }
    }

    public static void addErrorsToValidationDetail(List<ObjectError> globalErrors, List<FieldError> fieldErrors, ValidationDetail detail) {
        processGlobalErrors(detail, globalErrors, ObjectError::getObjectName);
        processFieldErrors(detail, fieldErrors, FieldError::getField);
    }

    public static void addValidationResultsToValidationDetail(Collection<ParameterValidationResult> results, ValidationDetail detail) {
        for (ParameterValidationResult result : results) {
            var parameterName = result.getMethodParameter().getParameterName();
            if (result instanceof ParameterErrors parameterErrors) {
                if (parameterErrors.getContainer() != null) {
                    processContainerErrors(parameterName, detail, parameterErrors);
                } else {
                    processGlobalErrors(detail, parameterErrors.getGlobalErrors(), ObjectError::getObjectName);
                    processFieldErrors(detail, parameterErrors.getFieldErrors(), FieldError::getField);
                }
            } else {
                processResolvableErrors(detail, result.getResolvableErrors(), resolvable -> parameterName);
            }
        }
    }

    private static String getViolationPath(Path propertyPath) {
        StringBuilder fieldPath = new StringBuilder();
        for (Path.Node node : propertyPath) {
            // Some elements of the path are unnecessary clutter, so they can be skipped in the final field path
            if (node.getKind() != ElementKind.PARAMETER && node.getKind() != ElementKind.PROPERTY) {
                continue;
            }

            if (!fieldPath.isEmpty()) {
                fieldPath.append(".");
            }
            fieldPath.append(node);
        }
        return fieldPath.toString();
    }

    private static void processContainerErrors(String containerName, ValidationDetail detail, ParameterErrors errors) {
        String elementPath;
        Class<?> containerClass = errors.getContainer().getClass();
        if (Iterable.class.isAssignableFrom(containerClass) || Map.class.isAssignableFrom(containerClass) || Object[].class.isAssignableFrom(containerClass)) {
            Object elementAccessor;
            if (errors.getContainerIndex() != null) {
                elementAccessor = errors.getContainerIndex();
            } else if (errors.getContainerKey() != null) {
                elementAccessor = errors.getContainerKey();
            } else {
                elementAccessor = "";
            }
            elementPath = ELEMENT_ACCESS_TEMPLATE.formatted(containerName, elementAccessor);
        } else {
            elementPath = containerName;
        }

        processGlobalErrors(detail, errors.getGlobalErrors(), globalError -> elementPath);
        processFieldErrors(detail, errors.getFieldErrors(), fieldError -> elementPath + "." + fieldError.getField());
    }

    private static void processFieldErrors(ValidationDetail detail, List<FieldError> fieldErrors, Function<FieldError, String> pathMapper) {
        for (FieldError fieldError : fieldErrors) {
            detail.addError(fieldError.getDefaultMessage(), pathMapper.apply(fieldError));
        }
    }

    private static void processGlobalErrors(ValidationDetail detail, List<ObjectError> globalErrors, Function<ObjectError, String> pathMapper) {
        for (ObjectError globalError : globalErrors) {
            detail.addError(globalError.getDefaultMessage(), pathMapper.apply(globalError));
        }
    }

    private static void processResolvableErrors(ValidationDetail detail, List<MessageSourceResolvable> resolvableErrors, Function<MessageSourceResolvable, String> pathMapper) {
        for (MessageSourceResolvable error : resolvableErrors) {
            detail.addError(error.getDefaultMessage(), pathMapper.apply(error));
        }
    }
}
