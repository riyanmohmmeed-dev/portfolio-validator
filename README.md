# SolvencyGuard 🛡️

**Enterprise Financial Validation Rules Engine**

SolvencyGuard is a robust, enterprise-grade REST API built with **Java 17** and **Spring Boot 3**. It acts as a Solvency II-inspired compliance engine, allowing financial institutions to validate investment portfolios against strict risk and exposure limits in real-time.

---

## 🚀 Features

- **Strategy Pattern Architecture**: Highly extensible rule execution engine. New compliance rules can be added seamlessly by implementing the `ComplianceRule` interface without modifying core service logic.
- **Real-Time Validation**: Accepts JSON array of financial holdings and processes them against defined limits.
- **Core Solvency Rules Implemented**:
  - `MaxEquityExposureRule`: Ensures total equity exposure never exceeds 40% of the Net Asset Value (NAV).
  - `MinimumLiquidityRule`: Enforces a hard floor of 15% in Cash or Cash Equivalents for emergency liquidity.
  - `SingleIssuerLimitRule`: Validates that no single non-cash asset constitutes more than 10% of the total NAV.
- **Comprehensive Testing**: Validated using JUnit 5 and Mockito, ensuring 100% core logic coverage.
- **Interactive API Documentation**: Fully documented via OpenAPI 3.0 (Swagger UI).

## 🛠️ Tech Stack

- **Backend**: Java 17, Spring Boot 3.4
- **Web**: Spring WebMVC, Spring Validation
- **Testing**: JUnit 5, Spring Boot Test
- **Documentation**: Springdoc OpenAPI / Swagger UI
- **Build Tool**: Maven

---

## 🚦 Getting Started

### Prerequisites
- JDK 17 or higher
- Maven 3.8+

### Running the Application

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/solvency-guard.git
   cd solvency-guard
   ```

2. Build and run via Maven Wrapper:
   ```bash
   ./mvnw spring-boot:run
   ```

3. The API will be available at `http://localhost:8080/api/v1/compliance/evaluate`.

---

## 📖 API Documentation (Swagger UI)

Once the application is running, you can interact with the API directly through the Swagger UI:

👉 `http://localhost:8080/swagger-ui.html`

### Example Request (`POST /api/v1/compliance/evaluate`)

```json
{
  "portfolioId": "PRT-90210",
  "baseCurrency": "EUR",
  "holdings": [
    {
      "isin": "US0378331005",
      "name": "Apple Inc.",
      "assetType": "EQUITY",
      "marketValueEur": 50000.00
    },
    {
      "isin": "LU0000000000",
      "name": "Euro Cash",
      "assetType": "CASH",
      "marketValueEur": 15000.00
    }
  ]
}
```

### Example Response

```json
{
  "portfolioId": "PRT-90210",
  "totalNavEur": 65000.0,
  "status": "NON_COMPLIANT",
  "evaluations": [
    {
      "ruleName": "MaxEquityExposureRule",
      "passed": false,
      "message": "Equity exposure is 76.92%, exceeding the 40.0% limit."
    },
    {
      "ruleName": "MinimumLiquidityRule",
      "passed": true,
      "message": "Cash exposure is 23.08%, which meets the 15.0% minimum."
    },
    {
      "ruleName": "SingleIssuerLimitRule",
      "passed": false,
      "message": "Holding 'Apple Inc.' (US0378331005) constitutes 76.92% of NAV, exceeding the 10.0% single issuer limit."
    }
  ]
}
```

---
*Built to demonstrate Enterprise Java patterns and Domain-Driven Design in the Financial Services domain.*
