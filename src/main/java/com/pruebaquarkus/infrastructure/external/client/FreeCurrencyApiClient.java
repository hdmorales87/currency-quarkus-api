package com.pruebaquarkus.infrastructure.external.client;

import com.pruebaquarkus.infrastructure.external.dto.FreeCurrencyApiResponseDTO;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.QueryParam;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;

@RegisterRestClient(configKey = "freecurrencyapi")
@RegisterProvider(FreeCurrencyApiKeyFilter.class)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface FreeCurrencyApiClient {
    
    @GET
    @Path("/v1/latest")
    FreeCurrencyApiResponseDTO getLatestRate(
        @QueryParam("base_currency") String baseCurrency,
        @QueryParam("currencies") String currencies
    );
    
}
