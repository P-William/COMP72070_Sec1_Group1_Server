package com.group11.accord.api.server;

import com.group11.accord.api.channel.Channel;
import com.group11.accord.api.user.Account;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.NonNull;

import java.time.LocalDateTime;
import java.util.List;

@Schema(
    name = "Server",
    description = "Represents a server in the system."
)
public record Server(

    @NonNull
    @Schema(description = "The server's id")
    Long id,

    @NonNull
    @Schema(description = "The server's name")
    String name,

    @NonNull
    @Schema(description = "The server's owner")
    Account owner,

    @NonNull
    @Schema(description = "When the server was created")
    LocalDateTime createdAt,

    @NonNull
    @Schema(description = "The channels in the server")
    List<Channel> channels
) { }
