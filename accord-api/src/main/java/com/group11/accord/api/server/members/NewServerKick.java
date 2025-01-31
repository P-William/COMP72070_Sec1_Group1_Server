package com.group11.accord.api.server.members;

import com.group11.accord.api.user.Account;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.NonNull;

public record NewServerKick(

        @NonNull
        @Schema(description = "The user to kick")
        Account kickedUser,

        @NonNull
        @Schema(description = "The user that is kicking the user")
        Account kickedBy,

        @NonNull
        @Schema(description = "The reason for the kick")
        String reason
) { }
