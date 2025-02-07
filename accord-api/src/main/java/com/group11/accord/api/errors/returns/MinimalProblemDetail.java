package com.group11.accord.api.errors.returns;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.net.URI;

@Schema(name = "ProblemDetail", description = "A detailed response for problems encountered")
@Getter
@NoArgsConstructor(access = AccessLevel.NONE)
public class MinimalProblemDetail {

    private URI type;

    @Schema(description = "The exception that caused the error.")
    private String title;

    @Schema(description = "HTTP status code returned by the API.")
    private int status;

    @Schema(description = "A short description of the error.")
    private String detail;

    @Schema(description = "A URI that identifies the specific instance of the problem.")
    private URI instance;
}
