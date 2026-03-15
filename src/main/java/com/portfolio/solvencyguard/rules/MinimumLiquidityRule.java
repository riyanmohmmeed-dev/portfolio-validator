package com.portfolio.solvencyguard.rules;

import com.portfolio.solvencyguard.model.AssetType;
import com.portfolio.solvencyguard.model.Holding;
import com.portfolio.solvencyguard.model.PortfolioRequest;
import com.portfolio.solvencyguard.model.RuleResult;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class MinimumLiquidityRule implements ComplianceRule {

    private static final BigDecimal MIN_CASH_PERCENTAGE = new BigDecimal("0.15"); // 15%

    @Override
    public RuleResult evaluate(PortfolioRequest portfolio, BigDecimal totalNav) {
        if (totalNav.compareTo(BigDecimal.ZERO) == 0) {
            return new RuleResult("MinimumLiquidityRule", false, "Portfolio is empty, insufficient liquidity.");
        }

        BigDecimal totalCash = portfolio.holdings().stream()
                .filter(h -> h.assetType() == AssetType.CASH)
                .map(Holding::marketValueEur)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal cashRatio = totalCash.divide(totalNav, 4, RoundingMode.HALF_UP);
        boolean passed = cashRatio.compareTo(MIN_CASH_PERCENTAGE) >= 0;

        String formattedRatio = cashRatio.multiply(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP) + "%";
        String message = passed 
            ? "Cash exposure is " + formattedRatio + ", which meets the 15.0% minimum."
            : "Cash exposure is " + formattedRatio + ", falling below the 15.0% minimum.";

        return new RuleResult("MinimumLiquidityRule", passed, message);
    }
}
