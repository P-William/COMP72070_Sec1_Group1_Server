package com.group11.accord.api.errors.returns;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Schema(name = "ValidationDetail", description = "A detailed response for validation errors")
@Getter
@NoArgsConstructor(access = AccessLevel.NONE)
public class MinimalValidationDetail extends MinimalProblemDetail {

    @Schema(description = "List of validation errors")
    private List<ValidationError> errors;

    private record ValidationError(
        @Schema(description = "The reason for the error") String message,
        @Schema(description = "What field the error is on") @Nullable String field
    ) { }

}
