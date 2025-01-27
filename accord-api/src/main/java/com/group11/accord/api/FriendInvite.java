package com.group11.accord.api;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.NonNull;

import java.time.LocalDateTime;

@Schema(
        name = "FriendInvite",
        description = "Represents a friend request from one account to another"
)
public record FriendInvite(
//        @NonNull
//        @Schema(description = "The FriendInvite id")
//        Long id,

        @NonNull
        @Schema(description = "The account that sent the invite")
        Account account,

        @NonNull
        @Schema(description = "The timestamp of when the invite was created")
        LocalDateTime sentAt
) {
}
