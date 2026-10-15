package com.pruebaquarkus.infrastructure.external.adapter;

import jakarta.enterprise.context.ApplicationScoped;

import com.pruebaquarkus.application.port.ExchangeRateApiPort;
import com.pruebaquarkus.domain.model.Currency;
import com.pruebaquarkus.infrastructure.external.client.FreeCurrencyApiClient;
import com.pruebaquarkus.infrastructure.external.dto.FreeCurrencyApiResponseDTO;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.math.BigDecimal;
import java.util.Optional;


@ApplicationScoped
public class FreeCurrencyApiAdapter implements ExchangeRateApiPort {
    private final FreeCurrencyApiClient freeCurrencyApiClient;
    
    public FreeCurrencyApiAdapter(
        @RestClient FreeCurrencyApiClient freeCurrencyApiClient
    ) {
        this.freeCurrencyApiClient = freeCurrencyApiClient;
    }
    
    @Override
    public Optional<BigDecimal> fetchRate(Currency from, Currency to) {
        try {
            System.out.println("Fetching rate from " + from.getCode() + " to " + to.getCode());
            FreeCurrencyApiResponseDTO response = freeCurrencyApiClient.getLatestRate(from.getCode(), to.getCode());
            System.out.println("Response: " + response);
            return Optional.ofNullable(response)
                    .map(r -> r.data().get(to.getCode()));
        } catch (Exception e) {
            System.err.println("Error fetching exchange rate: " + e.getMessage());
            e.printStackTrace();
            return Optional.empty();
        }
    }
}
