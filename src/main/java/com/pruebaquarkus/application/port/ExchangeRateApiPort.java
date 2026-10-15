package com.pruebaquarkus.application.port;

import java.math.BigDecimal;
import java.util.Optional;

import com.pruebaquarkus.domain.model.Currency;

public interface ExchangeRateApiPort {
    Optional<BigDecimal> fetchRate(Currency from, Currency to);
}
