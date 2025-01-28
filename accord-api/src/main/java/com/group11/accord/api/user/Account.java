package com.group11.accord.api.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.NonNull;

@Schema(
        name = "Account",
        description = "Represents a user in the system"
)
public record Account(
        @NonNull
        @Schema(description = "The account's id")
        Long id,

        @NonNull
        @Schema(description = "The account's username")
        String username,

        @NonNull
        @Schema(description = "The account's biography")
        String bio,

        @NonNull
        @Schema(description = "A link to the account's profile picture")
        String profile_picture
) { }
