package com.portfolio.solvencyguard.rules;

import com.portfolio.solvencyguard.model.AssetType;
import com.portfolio.solvencyguard.model.Holding;
import com.portfolio.solvencyguard.model.PortfolioRequest;
import com.portfolio.solvencyguard.model.RuleResult;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

@Component
public class SingleIssuerLimitRule implements ComplianceRule {

    private static final BigDecimal MAX_SINGLE_ISSUER_PERCENTAGE = new BigDecimal("0.10"); // 10%

    @Override
    public RuleResult evaluate(PortfolioRequest portfolio, BigDecimal totalNav) {
        if (totalNav.compareTo(BigDecimal.ZERO) == 0) {
            return new RuleResult("SingleIssuerLimitRule", true, "Portfolio is empty.");
        }

        // Find any non-cash asset that exceeds 10% of NAV
        Optional<Holding> violatingHolding = portfolio.holdings().stream()
                .filter(h -> h.assetType() != AssetType.CASH)
                .filter(h -> isViolating(h.marketValueEur(), totalNav))
                .findFirst();

        if (violatingHolding.isPresent()) {
            Holding h = violatingHolding.get();
            BigDecimal ratio = h.marketValueEur().divide(totalNav, 4, RoundingMode.HALF_UP);
            String formattedRatio = ratio.multiply(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP) + "%";
            return new RuleResult("SingleIssuerLimitRule", false, 
                "Holding '" + h.name() + "' (" + h.isin() + ") constitutes " + formattedRatio + " of NAV, exceeding the 10.0% single issuer limit.");
        }

        return new RuleResult("SingleIssuerLimitRule", true, "All non-cash holdings are within the 10.0% single issuer limit.");
    }

    private boolean isViolating(BigDecimal marketValue, BigDecimal totalNav) {
        BigDecimal ratio = marketValue.divide(totalNav, 4, RoundingMode.HALF_UP);
        return ratio.compareTo(MAX_SINGLE_ISSUER_PERCENTAGE) > 0;
    }
}
