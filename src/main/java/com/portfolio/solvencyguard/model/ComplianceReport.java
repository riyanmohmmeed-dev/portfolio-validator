package com.portfolio.solvencyguard.model;

import java.math.BigDecimal;
import java.util.List;


public record ComplianceReport(
        String portfolioId,
        BigDecimal totalNavEur,
        ComplianceStatus status,
        List<RuleResult> evaluations
) {}
