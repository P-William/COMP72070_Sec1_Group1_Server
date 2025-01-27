package com.group11.accord.api;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.NonNull;

import java.time.LocalDateTime;

@Schema(
        name = "ServerInvite",
        description = "Represents an invite for a user to join a server"
)
public record ServerInvite(
//        @NonNull
//        @Schema(description = "The ServerInvite id")
//        Long id,

        @NonNull
        @Schema(description = "The account that sent the invite")
        Account account,

        @NonNull
        @Schema(description = "The server the invite is for")
        Server server,

        @NonNull
        @Schema(description = "The timestamp of when the invite was created")
        LocalDateTime sentAt
) {
}
