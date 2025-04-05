package com.group11.accord.api.message;

import io.swagger.v3.oas.annotations.media.Schema;

public record NewImageMessage (

        @Schema(description = "The image sent as raw data")
        Byte[] image

) { }
