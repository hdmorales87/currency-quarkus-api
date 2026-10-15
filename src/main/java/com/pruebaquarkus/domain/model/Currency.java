package com.pruebaquarkus.domain.model;

public class Currency {
    private String code; 
    
    public Currency(String code) {
        validate(code);
        this.code = code;
    }
    
    public void validate(String code) {
        if (code == null || code.trim().isEmpty()) {
            throw new IllegalArgumentException("Currency code cannot be null or empty");
        }
        
        String normalizedCode = code.trim().toUpperCase();
        if (!normalizedCode.matches("^[A-Z]{3}$")) {
            throw new IllegalArgumentException("Currency code must be a 3-letter ISO code");
        }
    }

    public String getCode() {
        return code;
    }

    @Override 
    public String toString() {
        return "Currency{" +
                "code='" + code + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Currency currency = (Currency) obj;
        return code.equals(currency.code);
    }
    
    @Override
    public int hashCode() {
        return code.hashCode();
    }
    
}
