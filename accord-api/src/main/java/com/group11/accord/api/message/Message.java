package com.group11.accord.api.message;

import com.group11.accord.api.user.Account;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.NonNull;

import java.time.LocalDateTime;

@Schema(
        name = "Message",
        description = "Represents a message in a channel"
)
public record Message(
        @NonNull
        @Schema(description = "The messages id")
        Long id,

        @NonNull
        @Schema(description = "The author of the message")
        Account account,

        @NonNull
        @Schema(description = "The ID of the channel the message was sent in")
        Long channelId,

        @NonNull
        @Schema(description = "The content/body of the message")
        String content,

        @NonNull
        @Schema(description = "If the message body is a link to an image or text")
        Boolean isImage,

        @NonNull
        @Schema(description = "The timestamp from when the message was sent")
        LocalDateTime sentAt
) { }
