package com.pruebaquarkus.infrastructure.external.client;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.client.ClientRequestContext;
import jakarta.ws.rs.client.ClientRequestFilter;
import jakarta.ws.rs.core.UriBuilder;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.io.IOException;
import java.net.URI;

@ApplicationScoped 
public class FreeCurrencyApiKeyFilter implements ClientRequestFilter {
    @ConfigProperty(name = "freecurrencyapi.key")
    String apiKey;

    @Override
    public void filter(ClientRequestContext requestContext) throws IOException {
        System.out.println("API Key from config: " + apiKey);
        URI uri = requestContext.getUri();
        System.out.println("Original URI: " + uri);
        URI newUri = UriBuilder.fromUri(uri)
            .queryParam("apikey", apiKey)
            .build();
        System.out.println("Modified URI: " + newUri);
        requestContext.setUri(newUri);
    }
    
}
