package com.portfolio.solvencyguard.controller;

import com.portfolio.solvencyguard.model.ComplianceReport;
import com.portfolio.solvencyguard.model.PortfolioRequest;
import com.portfolio.solvencyguard.service.ComplianceEngineService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/compliance")
public class ComplianceController {

    private final ComplianceEngineService engineService;

    public ComplianceController(ComplianceEngineService engineService) {
        this.engineService = engineService;
    }

    @PostMapping("/evaluate")
    public ResponseEntity<ComplianceReport> evaluatePortfolio(@Valid @RequestBody PortfolioRequest request) {
        ComplianceReport report = engineService.evaluatePortfolio(request);
        return ResponseEntity.ok(report);
    }
}
