package com.example.server.exception;

import lombok.Getter;

@Getter
public class NoLocalizationFoundException extends RuntimeException {

    public Long localizationId;

    public NoLocalizationFoundException(Long localizationId) {
        this.localizationId = localizationId;
    }
}
