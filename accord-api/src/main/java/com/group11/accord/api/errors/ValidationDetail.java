package com.group11.accord.api.errors;

import jakarta.annotation.Nullable;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

import java.util.ArrayList;
import java.util.Collection;

@EqualsAndHashCode(callSuper = false)
public class ValidationDetail extends ProblemDetail {
    public static final String DEFAULT_MESSAGE = "Validation error encountered. See errors for details.";

    private final Collection<ValidationError> errors = new ArrayList<>();

    ValidationDetail(ProblemDetail problemDetail) {
        super(problemDetail);
        this.setDetail(DEFAULT_MESSAGE);
        this.setProperty("errors", errors);
    }

    ValidationDetail() {
        super(HttpStatus.BAD_REQUEST.value());
        this.setTitle("Validation Error");
        this.setDetail(DEFAULT_MESSAGE);
        this.setProperty("errors", errors);
    }

    public void addError(String message, String path) {
        errors.add(new ValidationError(message, path));
    }

    private record ValidationError(String message, @Nullable String field) { }
}
