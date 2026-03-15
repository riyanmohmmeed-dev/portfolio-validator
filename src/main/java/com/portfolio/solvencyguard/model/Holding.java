package com.portfolio.solvencyguard.model;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record Holding(
        @NotBlank(message = "ISIN must not be blank")
        String isin,
        
        @NotBlank(message = "Name must not be blank")
        String name,
        
        @NotNull(message = "Asset Type is required")
        AssetType assetType,
        
        @NotNull(message = "Market Value is required")
        @DecimalMin(value = "0.0", inclusive = false, message = "Market value must be strictly positive")
        BigDecimal marketValueEur
) {}
