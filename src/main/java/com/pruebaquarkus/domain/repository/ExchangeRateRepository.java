package com.pruebaquarkus.domain.repository;

import com.pruebaquarkus.domain.model.ExchangeRate;
import com.pruebaquarkus.domain.model.Currency;

import java.util.Optional;

public interface ExchangeRateRepository {
    Optional<ExchangeRate> findExchangeRate(Currency from, Currency to);
    ExchangeRate save(ExchangeRate exchangeRate);
}
