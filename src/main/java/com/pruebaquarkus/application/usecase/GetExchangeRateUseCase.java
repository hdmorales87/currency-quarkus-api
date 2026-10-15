package com.pruebaquarkus.application.usecase;

import com.pruebaquarkus.application.dto.ExchangeRateRequest;
import com.pruebaquarkus.application.dto.ExchangeRateResponse;
import com.pruebaquarkus.domain.model.ExchangeRate;
import com.pruebaquarkus.domain.model.Currency;
import com.pruebaquarkus.domain.repository.ExchangeRateRepository;
import com.pruebaquarkus.application.port.ExchangeRateApiPort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;

@ApplicationScoped
public class GetExchangeRateUseCase {

    private final ExchangeRateRepository exchangeRateRepository;
    private final ExchangeRateApiPort exchangeRateApiPort;

    public GetExchangeRateUseCase(
        ExchangeRateRepository exchangeRateRepository, 
        ExchangeRateApiPort exchangeRateApiPort
    ) {
        this.exchangeRateRepository = exchangeRateRepository;
        this.exchangeRateApiPort = exchangeRateApiPort;
    }

    @Transactional
    public ExchangeRateResponse execute(ExchangeRateRequest request) {
        Currency from = new Currency(request.from());
        Currency to = new Currency(request.to());
        
        return exchangeRateRepository.findExchangeRate(from, to)
            .map(this::mapToResponse)
            .orElseGet(() -> fetchAndCache(from, to));
    }

    private ExchangeRateResponse fetchAndCache(Currency from, Currency to) {
        BigDecimal rate = exchangeRateApiPort.fetchRate(from, to).orElseThrow(() -> new RuntimeException("Exchange rate not found"));
        ExchangeRate exchangeRate = new ExchangeRate(from, to, rate);
        exchangeRateRepository.save(exchangeRate);
        return mapToResponse(exchangeRate);
    }

    private ExchangeRateResponse mapToResponse(ExchangeRate exchangeRate) {
        return new ExchangeRateResponse(
            exchangeRate.getFrom().getCode(), 
            exchangeRate.getTo().getCode(), 
            exchangeRate.getRate()
        );
    }

}
