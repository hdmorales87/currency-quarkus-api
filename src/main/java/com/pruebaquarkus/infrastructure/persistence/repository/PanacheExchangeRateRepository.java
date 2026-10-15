package com.pruebaquarkus.infrastructure.persistence.repository;

import jakarta.enterprise.context.ApplicationScoped;
import java.util.Optional;

import com.pruebaquarkus.domain.model.Currency;
import com.pruebaquarkus.infrastructure.persistence.entity.ExchangeRateEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class PanacheExchangeRateRepository implements PanacheRepository<ExchangeRateEntity>{
    public Optional<ExchangeRateEntity> findByCurrencies(Currency from, Currency to) {
        return find("fromCurrency = ?1 and toCurrency = ?2",
            from.getCode(), to.getCode()
        ).firstResultOptional();
    }
}
