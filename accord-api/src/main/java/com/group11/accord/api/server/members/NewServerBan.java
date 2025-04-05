package com.group11.accord.api.server.members;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.NonNull;

public record NewServerBan(

        @NonNull
        @Schema(description = "The user to ban")
        Long bannedUserId,

        @NonNull
        @Schema(description = "The user that is banning the user")
        Long bannedById,

        @NonNull
        @Schema(description = "The reason for the ban")
        @Size(min = 20, max = 512, message = "Reasoning must be between 20 and 512 characters")
        String reason
) { }
