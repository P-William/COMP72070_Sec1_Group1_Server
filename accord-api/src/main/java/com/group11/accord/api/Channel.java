package com.group11.accord.api;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.NonNull;

import java.time.LocalDateTime;

@Schema(
    name = "Channel",
    description = "Represents a channel in a server or direct message."
)
public record Channel(
    @NonNull
    @Schema(description = "The channel's id")
    Long id,

    @NonNull
    @Schema(description = "The channel's name")
    String name,

    @NonNull
    @Schema(description = "If the channel is part of a server or a DM")
    Boolean isDM,

    @NonNull
    @Schema(description = "When the channel was created")
    LocalDateTime createdAt
) { }
