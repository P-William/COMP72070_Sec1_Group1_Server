package com.group11.accord.api.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(
        name = "AccountUpload",
        description = "Account information used for account creation"
)
public record NewAccount(
        @Schema(description = "Username for the account")
        @NotBlank(message = "Username cannot be blank")
        @Size(min = 6, max = 32, message = "Username must be between 6 and 32 characters long")
        String username,

        @Schema(description = "The password for the new account")
        @NotBlank(message = "Password cannot be blank")
        @Size(min = 8, max = 32, message = "Password must be between 6 and 32 characters long")
        String password,

        @Schema(description = "The accounts bio")
        @Size(max = 512, message = "User biography can only be a maximum of 512 characters")
        String bio
) { }
