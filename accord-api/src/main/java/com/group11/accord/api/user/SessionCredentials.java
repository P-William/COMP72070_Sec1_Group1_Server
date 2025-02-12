package com.group11.accord.api.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.NonNull;

public record SessionCredentials (

        @NonNull
        @Schema(description = "The accounts id")
        Long id,

        @NonNull
        @Schema(description = "The session token. Note: uniquely generated each time an account is logged in")
        String token

) { }
