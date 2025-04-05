package com.group11.accord.api.server.members;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.NonNull;

public record NewServerKick(

        @NonNull
        @Schema(description = "The user to kick")
        Long kickedUserId,

        @NonNull
        @Schema(description = "The user that is kicking the user")
        Long kickedById,

        @NonNull
        @Schema(description = "The reason for the kick")
        @Size(min = 20, max = 512, message = "Reasoning must be between 20 and 512 characters")
        String reason
) { }
