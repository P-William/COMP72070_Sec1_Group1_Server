package com.group11.accord.api.server;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.NonNull;

@Schema(
    name = "ServerDeletion",
    description = "Represents a server being deleted."
)
public record ServerDeletion(
    @NonNull
    @Schema(description = "The server's id")
    Long id,

    @NonNull
    @Schema(description = "The server's name")
    String name
) { }
