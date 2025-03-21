package com.group11.accord.api.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
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

        @Schema(description = "The account's biography")
        @Size(max = 512, message = "Users biography can be a maximum of 512 characters")
        String bio,
        
        @Schema(description = "A link to the account's profile picture")
        String profilePicUrl
) { }
