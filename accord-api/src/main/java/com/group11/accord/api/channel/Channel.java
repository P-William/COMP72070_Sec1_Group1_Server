package com.group11.accord.api.channel;

import com.group11.accord.api.message.Message;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.NonNull;

import java.time.LocalDateTime;
import java.util.List;

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
    @Schema(description = "A list of all the messages sent in the channel")
    List<Message> messages,

    @NonNull
    @Schema(description = "If the channel is part of a server or a DM")
    Boolean isDM,

    @NonNull
    @Schema(description = "When the channel was created")
    LocalDateTime createdAt
) { }
