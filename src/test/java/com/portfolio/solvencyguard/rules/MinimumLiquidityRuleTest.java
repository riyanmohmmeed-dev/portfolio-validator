package com.portfolio.solvencyguard.rules;

import com.portfolio.solvencyguard.model.AssetType;
import com.portfolio.solvencyguard.model.Holding;
import com.portfolio.solvencyguard.model.PortfolioRequest;
import com.portfolio.solvencyguard.model.RuleResult;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MinimumLiquidityRuleTest {

    private final MinimumLiquidityRule rule = new MinimumLiquidityRule();

    @Test
    void shouldPassWhenCashIsAboveMinimum() {
        PortfolioRequest request = new PortfolioRequest("PRT-1", "EUR", List.of(
                new Holding("ISIN1", "Euro Cash", AssetType.CASH, new BigDecimal("2000")),
                new Holding("ISIN2", "Gov Bond", AssetType.BOND, new BigDecimal("8000"))
        ));
        BigDecimal nav = new BigDecimal("10000");

        RuleResult result = rule.evaluate(request, nav);

        assertTrue(result.passed());
        assertEquals("MinimumLiquidityRule", result.ruleName());
        assertTrue(result.message().contains("20.00%"));
    }

    @Test
    void shouldFailWhenCashIsBelowMinimum() {
        PortfolioRequest request = new PortfolioRequest("PRT-1", "EUR", List.of(
                new Holding("ISIN1", "Euro Cash", AssetType.CASH, new BigDecimal("1000")),
                new Holding("ISIN2", "Gov Bond", AssetType.BOND, new BigDecimal("9000"))
        ));
        BigDecimal nav = new BigDecimal("10000");

        RuleResult result = rule.evaluate(request, nav);

        assertFalse(result.passed());
        assertTrue(result.message().contains("10.00%"));
    }
}
