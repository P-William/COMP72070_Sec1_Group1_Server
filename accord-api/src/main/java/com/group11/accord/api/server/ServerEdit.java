package com.group11.accord.api.server;

import com.group11.accord.api.user.Account;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.NonNull;

@Schema(
    name = "ServerEdit",
    description = "Represents an edit to a server."
)
public record ServerEdit(
    @NonNull
    @Schema(description = "The server's id")
    Long id,

    @NonNull
    @Schema(description = "The server's name")
    String name,

    @NonNull
    @Schema(description = "The server's owner")
    Account owner
) { }
