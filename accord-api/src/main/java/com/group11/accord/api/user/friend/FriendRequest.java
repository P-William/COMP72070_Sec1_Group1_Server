package com.group11.accord.api.user.friend;

import com.group11.accord.api.user.Account;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.NonNull;

import java.time.LocalDateTime;

@Schema(
        name = "FriendInvite",
        description = "Represents a friend request from one account to another"
)
public record FriendRequest(
        @NonNull
        @Schema(description = "The FriendInvite id")
        Long id,

        @NonNull
        @Schema(description = "The account that sent the invite")
        Account sender,

        @NonNull
        @Schema(description = "The account that received the invite")
        Account recipient,

        @NonNull
        @Schema(description = "The timestamp of when the invite was created")
        LocalDateTime sentAt
) {
}
