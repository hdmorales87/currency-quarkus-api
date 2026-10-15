package com.pruebaquarkus.presentation;

import com.pruebaquarkus.application.usecase.GetExchangeRateUseCase;
import com.pruebaquarkus.application.dto.ExchangeRateRequest;
import com.pruebaquarkus.application.dto.ExchangeRateResponse;

import jakarta.ws.rs.Path;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;

@Path("/exchange")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ExchangeRateController {
    private final GetExchangeRateUseCase getExchangeRateUseCase;
    
    public ExchangeRateController(GetExchangeRateUseCase getExchangeRateUseCase) {
        this.getExchangeRateUseCase = getExchangeRateUseCase;
    }
    
    @GET
    @Path("/from/{from}/to/{to}")
    public Response getExchangeRate(@PathParam("from") String from, @PathParam("to") String to) {
        ExchangeRateRequest request = new ExchangeRateRequest(from, to);
        ExchangeRateResponse response = getExchangeRateUseCase.execute(request);
        return Response.ok(response).build();
    }
}
