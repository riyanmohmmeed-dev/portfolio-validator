package com.portfolio.solvencyguard.rules;

import com.portfolio.solvencyguard.model.PortfolioRequest;
import com.portfolio.solvencyguard.model.RuleResult;
import java.math.BigDecimal;

public interface ComplianceRule {
    RuleResult evaluate(PortfolioRequest portfolio, BigDecimal totalNav);
}
