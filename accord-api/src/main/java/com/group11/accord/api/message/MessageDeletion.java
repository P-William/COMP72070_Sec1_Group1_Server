package com.group11.accord.api.message;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.NonNull;

import java.time.LocalDateTime;

@Schema(
    name = "MessageDeletion",
    description = "Represents a message deletion."
)
public record MessageDeletion(
    @NonNull
    @Schema(description = "The id of the message being deelted.")
    Long id,

    @NonNull
    @Schema(description = "The id of the channel the message is in.")
    Long channelId,

    @NonNull
    @Schema(description = "The id of the server the message is in.")
    Long serverId,

    @NonNull
    @Schema(description = "The timestamp of the deletion.")
    LocalDateTime deletedAt
) { }
