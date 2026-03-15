package com.portfolio.solvencyguard.rules;

import com.portfolio.solvencyguard.model.AssetType;
import com.portfolio.solvencyguard.model.Holding;
import com.portfolio.solvencyguard.model.PortfolioRequest;
import com.portfolio.solvencyguard.model.RuleResult;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MaxEquityExposureRuleTest {

    private final MaxEquityExposureRule rule = new MaxEquityExposureRule();

    @Test
    void shouldPassWhenEquityIsBelowLimit() {
        PortfolioRequest request = new PortfolioRequest("PRT-1", "EUR", List.of(
                new Holding("ISIN1", "Apple", AssetType.EQUITY, new BigDecimal("3000")),
                new Holding("ISIN2", "Gov Bond", AssetType.BOND, new BigDecimal("7000"))
        ));
        BigDecimal nav = new BigDecimal("10000");

        RuleResult result = rule.evaluate(request, nav);

        assertTrue(result.passed());
        assertEquals("MaxEquityExposureRule", result.ruleName());
        assertTrue(result.message().contains("30.00%"));
    }

    @Test
    void shouldFailWhenEquityExceedsLimit() {
        PortfolioRequest request = new PortfolioRequest("PRT-1", "EUR", List.of(
                new Holding("ISIN1", "Apple", AssetType.EQUITY, new BigDecimal("5000")),
                new Holding("ISIN2", "Gov Bond", AssetType.BOND, new BigDecimal("5000"))
        ));
        BigDecimal nav = new BigDecimal("10000");

        RuleResult result = rule.evaluate(request, nav);

        assertFalse(result.passed());
        assertTrue(result.message().contains("50.00%"));
    }
}
