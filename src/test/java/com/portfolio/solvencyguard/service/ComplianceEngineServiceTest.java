package com.portfolio.solvencyguard.service;

import com.portfolio.solvencyguard.model.AssetType;
import com.portfolio.solvencyguard.model.ComplianceReport;
import com.portfolio.solvencyguard.model.ComplianceStatus;
import com.portfolio.solvencyguard.model.Holding;
import com.portfolio.solvencyguard.model.PortfolioRequest;
import com.portfolio.solvencyguard.rules.ComplianceRule;
import com.portfolio.solvencyguard.rules.MaxEquityExposureRule;
import com.portfolio.solvencyguard.rules.MinimumLiquidityRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ComplianceEngineServiceTest {

    private ComplianceEngineService service;

    @BeforeEach
    void setUp() {
        List<ComplianceRule> rules = List.of(
                new MaxEquityExposureRule(),
                new MinimumLiquidityRule()
        );
        service = new ComplianceEngineService(rules);
    }

    @Test
    void shouldReturnCompliantForValidPortfolio() {
        PortfolioRequest request = new PortfolioRequest("PRT-1", "EUR", List.of(
                new Holding("ISIN1", "Equities", AssetType.EQUITY, new BigDecimal("3000")),
                new Holding("ISIN2", "Cash", AssetType.CASH, new BigDecimal("2000")),
                new Holding("ISIN3", "Bonds", AssetType.BOND, new BigDecimal("5000"))
        ));

        ComplianceReport report = service.evaluatePortfolio(request);

        assertEquals(ComplianceStatus.COMPLIANT, report.status());
        assertEquals(new BigDecimal("10000"), report.totalNavEur());
        assertEquals(2, report.evaluations().size());
        assertTrue(report.evaluations().get(0).passed());
        assertTrue(report.evaluations().get(1).passed());
    }

    @Test
    void shouldReturnNonCompliantForInvalidPortfolio() {
        PortfolioRequest request = new PortfolioRequest("PRT-1", "EUR", List.of(
                new Holding("ISIN1", "Equities", AssetType.EQUITY, new BigDecimal("6000")), // Fails Max Equity
                new Holding("ISIN2", "Cash", AssetType.CASH, new BigDecimal("1000")),    // Fails Min Liquidity
                new Holding("ISIN3", "Bonds", AssetType.BOND, new BigDecimal("3000"))
        ));

        ComplianceReport report = service.evaluatePortfolio(request);

        assertEquals(ComplianceStatus.NON_COMPLIANT, report.status());
        assertEquals(new BigDecimal("10000"), report.totalNavEur());
        assertFalse(report.evaluations().get(0).passed()); // MaxEquity failed
        assertFalse(report.evaluations().get(1).passed()); // MinLiquidity failed
    }
}
