package com.group11.accord.api.message;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record NewTextMessage(

    @Schema(description = "The message's content")
    @NotNull(message = "Content is required")
    String content

) {
}
