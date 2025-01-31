package com.group11.accord.api.message;

import io.swagger.v3.oas.annotations.media.Schema;

public record NewTextMessage (

    @Schema(description = "The message's content")
    String content

) { }
