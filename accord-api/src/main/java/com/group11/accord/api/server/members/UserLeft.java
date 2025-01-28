package com.group11.accord.api.server.members;

import com.group11.accord.api.server.BasicServer;
import com.group11.accord.api.user.Account;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.NonNull;

import java.time.LocalDateTime;

@Schema(
    name = "UserLeft",
    description = "Represents a user leaving a server."
)
public record UserLeft(
    @NonNull
    @Schema(description = "The server the user left")
    BasicServer server,

    @NonNull
    @Schema(description = "The user that left")
    Account account,

    @NonNull
    @Schema(description = "When the user left")
    LocalDateTime leftAt
) { }
