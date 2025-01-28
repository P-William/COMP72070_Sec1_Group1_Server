package com.group11.accord.api.server.members;

import com.group11.accord.api.server.BasicServer;
import com.group11.accord.api.user.Account;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.NonNull;

import java.time.LocalDateTime;

@Schema(
    name = "UserAdded",
    description = "Represents a user being added to a server."
)
public record UserAdded(
    @NonNull
    @Schema(description = "The server the user was added to")
    BasicServer server,

    @NonNull
    @Schema(description = "The user that was added")
    Account account,

    @NonNull
    @Schema(description = "The user that added the user")
    Account invitedBy,

    @NonNull
    @Schema(description = "When the user was added")
    LocalDateTime joinedAt
) { }
