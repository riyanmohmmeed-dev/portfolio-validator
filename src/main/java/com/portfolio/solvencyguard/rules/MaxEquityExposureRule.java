package com.portfolio.solvencyguard.rules;

import com.portfolio.solvencyguard.model.AssetType;
import com.portfolio.solvencyguard.model.Holding;
import com.portfolio.solvencyguard.model.PortfolioRequest;
import com.portfolio.solvencyguard.model.RuleResult;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class MaxEquityExposureRule implements ComplianceRule {

    private static final BigDecimal MAX_EQUITY_PERCENTAGE = new BigDecimal("0.40"); // 40%

    @Override
    public RuleResult evaluate(PortfolioRequest portfolio, BigDecimal totalNav) {
        if (totalNav.compareTo(BigDecimal.ZERO) == 0) {
            return new RuleResult("MaxEquityExposureRule", true, "Portfolio is empty.");
        }

        BigDecimal totalEquity = portfolio.holdings().stream()
                .filter(h -> h.assetType() == AssetType.EQUITY)
                .map(Holding::marketValueEur)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal equityRatio = totalEquity.divide(totalNav, 4, RoundingMode.HALF_UP);
        boolean passed = equityRatio.compareTo(MAX_EQUITY_PERCENTAGE) <= 0;

        String formattedRatio = equityRatio.multiply(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP) + "%";
        String message = passed 
            ? "Equity exposure is " + formattedRatio + ", which is within the 40.0% limit."
            : "Equity exposure is " + formattedRatio + ", exceeding the 40.0% limit.";

        return new RuleResult("MaxEquityExposureRule", passed, message);
    }
}
