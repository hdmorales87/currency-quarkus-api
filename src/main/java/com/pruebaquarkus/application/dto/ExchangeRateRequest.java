package com.pruebaquarkus.application.dto;

public record ExchangeRateRequest(
    String from,
    String to
) {
}
