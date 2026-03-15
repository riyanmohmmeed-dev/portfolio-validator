package com.portfolio.solvencyguard.model;

public record RuleResult(
        String ruleName,
        boolean passed,
        String message
) {}
