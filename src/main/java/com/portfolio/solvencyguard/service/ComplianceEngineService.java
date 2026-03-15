package com.portfolio.solvencyguard.service;

import com.portfolio.solvencyguard.model.ComplianceReport;
import com.portfolio.solvencyguard.model.ComplianceStatus;
import com.portfolio.solvencyguard.model.Holding;
import com.portfolio.solvencyguard.model.PortfolioRequest;
import com.portfolio.solvencyguard.model.RuleResult;
import com.portfolio.solvencyguard.rules.ComplianceRule;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ComplianceEngineService {

    // Spring automatically injects all beans implementing ComplianceRule
    private final List<ComplianceRule> rules;

    public ComplianceEngineService(List<ComplianceRule> rules) {
        this.rules = rules;
    }

    public ComplianceReport evaluatePortfolio(PortfolioRequest request) {
        BigDecimal totalNav = calculateTotalNav(request.holdings());

        // Execute all injected rules using the Strategy Pattern
        List<RuleResult> ruleResults = rules.stream()
                .map(rule -> rule.evaluate(request, totalNav))
                .collect(Collectors.toList());

        // Determine overall compliance
        boolean isCompliant = ruleResults.stream().allMatch(RuleResult::passed);
        ComplianceStatus status = isCompliant ? ComplianceStatus.COMPLIANT : ComplianceStatus.NON_COMPLIANT;

        return new ComplianceReport(
                request.portfolioId(),
                totalNav,
                status,
                ruleResults
        );
    }

    private BigDecimal calculateTotalNav(List<Holding> holdings) {
        if (holdings == null || holdings.isEmpty()) {
            return BigDecimal.ZERO;
        }
        
        return holdings.stream()
                .map(Holding::marketValueEur)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
