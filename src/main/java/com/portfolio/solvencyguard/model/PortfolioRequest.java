package com.portfolio.solvencyguard.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record PortfolioRequest(
        @NotBlank(message = "Portfolio ID must not be blank")
        String portfolioId,
        
        @NotBlank(message = "Base Currency must not be blank")
        String baseCurrency,
        
        @NotEmpty(message = "Portfolio must contain at least one holding")
        @Valid
        List<Holding> holdings
) {}
