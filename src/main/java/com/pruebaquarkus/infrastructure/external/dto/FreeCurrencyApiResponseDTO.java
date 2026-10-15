package com.pruebaquarkus.infrastructure.external.dto;

import java.math.BigDecimal;
import java.util.Map;

public record FreeCurrencyApiResponseDTO(
    Map<String, BigDecimal> data
) { }

/**
 * DTO for FreeCurrencyApi response
 * {
 *   "data": {
 *     "USD": 1.0
 *   }
 * }
 */