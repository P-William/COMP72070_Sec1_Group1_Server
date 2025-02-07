package com.group11.accord.api.server.members;

import com.group11.accord.api.user.Account;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.NonNull;

public record NewServerBan(

        @NonNull
        @Schema(description = "The user to ban")
        Account bannedUser,

        @NonNull
        @Schema(description = "The user that is banning the user")
        Account bannedBy,

        @NonNull
        @Schema(description = "The reason for the ban")
        String reason
) { }
