package com.pruebaquarkus.application.dto;

import java.math.BigDecimal;

public record ExchangeRateResponse(
    String from,
    String to,
    BigDecimal rate
) {
}
