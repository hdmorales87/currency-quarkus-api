package com.pruebaquarkus.infrastructure.persistence.adapter;

import com.pruebaquarkus.domain.model.Currency;
import com.pruebaquarkus.domain.model.ExchangeRate;
import com.pruebaquarkus.domain.repository.ExchangeRateRepository;
import com.pruebaquarkus.infrastructure.persistence.entity.ExchangeRateEntity;
import com.pruebaquarkus.infrastructure.persistence.repository.PanacheExchangeRateRepository;

import jakarta.enterprise.context.ApplicationScoped;
import java.util.Optional;


@ApplicationScoped
public class ExchangeRateRepositoryAdapter implements ExchangeRateRepository {
    private final PanacheExchangeRateRepository panacheExchangeRateRepository;
    
    public ExchangeRateRepositoryAdapter(PanacheExchangeRateRepository panacheExchangeRateRepository) {
        this.panacheExchangeRateRepository = panacheExchangeRateRepository;
    }  
   
    @Override
    public Optional<ExchangeRate> findExchangeRate(com.pruebaquarkus.domain.model.Currency from,
            com.pruebaquarkus.domain.model.Currency to) {
        return panacheExchangeRateRepository.findByCurrencies(from, to)
            .map(this::toDomain);
    }

     @Override
    public ExchangeRate save(ExchangeRate exchangeRate) {
        ExchangeRateEntity entity = toEntity(exchangeRate);
        panacheExchangeRateRepository.persist(entity);
        return toDomain(entity);
    }

    private ExchangeRate toDomain(ExchangeRateEntity entity) {
        return new ExchangeRate(
            new Currency(entity.getFromCurrency()),
            new Currency(entity.getToCurrency()),
            entity.getRate()
        );
    }

    private ExchangeRateEntity toEntity(ExchangeRate exchangeRate) {
        ExchangeRateEntity entity = new ExchangeRateEntity();
        entity.setFromCurrency(exchangeRate.getFrom().getCode());
        entity.setToCurrency(exchangeRate.getTo().getCode());
        entity.setRate(exchangeRate.getRate());
        return entity;
    }
}
