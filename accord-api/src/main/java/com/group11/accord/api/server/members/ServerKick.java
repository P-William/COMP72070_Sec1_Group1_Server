package com.group11.accord.api.server.members;

import com.group11.accord.api.server.BasicServer;
import com.group11.accord.api.user.Account;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.NonNull;

import java.time.LocalDateTime;

@Schema(
    name = "ServerKick",
    description = "Represents a kick of a user from a server."
)
public record ServerKick(

    @NonNull
    @Schema(description = "The kick's id")
    Long id,

    @NonNull
    @Schema(description = "The server the user was kicked from")
    BasicServer server,

    @NonNull
    @Schema(description = "The user that was kicked")
    Account kickedUser,

    @NonNull
    @Schema(description = "The user that kicked the user")
    Account kickedBy,

    @NonNull
    @Schema(description = "When the user was kicked")
    LocalDateTime kickedAt,

    @NonNull
    @Schema(description = "The reason for the kick")
    String reason
) { }
