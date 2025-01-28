package com.group11.accord.api.message;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.NonNull;

import java.time.LocalDateTime;

@Schema(
    name = "MessageEdit",
    description = "Represents an edit to a message."
)
public record MessageEdit(
    @NonNull
    @Schema(description = "The id of the message being edited.")
    Long id,

    @NonNull
    @Schema(description = "The id of the channel the message is in.")
    Long channelId,

    @NonNull
    @Schema(description = "The id of the server the message is in.")
    Long serverId,

    @NonNull
    @Schema(description = "The new content of the message.")
    String newContent,

    @NonNull
    @Schema(description = "The timestamp of the edit.")
    LocalDateTime editedAt
) { }
