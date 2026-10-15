package com.pruebaquarkus.domain.model;

import java.math.BigDecimal;

public class ExchangeRate {
    private final Currency from;
    private final Currency to;
    private final BigDecimal rate;
    
    public ExchangeRate(
        Currency from, 
        Currency to, 
        BigDecimal rate
    ) {
        validate(from, to, rate);
        this.from = from;
        this.to = to;
        this.rate = rate;
    }
    
    private void validate(
        Currency from, 
        Currency to, 
        BigDecimal rate
    ) {
        if (from == null) {
            throw new IllegalArgumentException("From currency cannot be null");
        }
        if (to == null) {
            throw new IllegalArgumentException("To currency cannot be null");
        }
        if (from.equals(to)) {
            throw new IllegalArgumentException("From and to currencies cannot be the same");
        }
        if (rate == null || rate.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Rate must be positive");
        }
    }
    
    public Currency getFrom() {
        return from;
    }
    
    public Currency getTo() {
        return to;
    }
    
    public BigDecimal getRate() {
        return rate;
    }
}
