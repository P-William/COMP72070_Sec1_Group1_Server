package com.group11.accord.api;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.NonNull;

import java.time.LocalDateTime;

@Schema(
    name = "ServerBan",
    description = "Represents a ban of a user from a server."
)
public record ServerBan(

    @NonNull
    @Schema(description = "The ban's id")
    Long id,

    @NonNull
    @Schema(description = "The server the user was banned from")
    Server server,

    @NonNull
    @Schema(description = "The user that was banned")
    User bannedUser,

    @NonNull
    @Schema(description = "The user that banned the user")
    User bannedBy,

    @NonNull
    @Schema(description = "When the user was banned")
    LocalDateTime bannedAt,

    @NonNull
    @Schema(description = "The reason for the ban")
    String reason
) { }
