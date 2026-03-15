# SolvencyGuard

A Java/Spring Boot rules engine that checks if financial investment portfolios are legally compliant.

## What is it?
In the financial world, investment funds have strict rules. For example, a mutual fund might not be legally allowed to have more than 40% of its money in stocks, or it must keep at least 15% in cash for emergencies. 

Instead of writing one massive `if/else` block, I built **SolvencyGuard**, a back-end REST API that uses the **Strategy Design Pattern**. It takes in a JSON list of investments (a portfolio) and runs them through a dynamic list of compliance rules to calculate if the fund is "Compliant" or "Non-Compliant".

## Technologies Used
* **Java 17** 
* **Spring Boot 3** (Web, Validation)
* **JUnit 5 & Mockito** (For testing the financial math logic)
* **Swagger UI / OpenAPI** (For interactive API documentation)
* **Maven**

## How to run it locally

1. Open your terminal and clone the repository:
```bash
git clone https://github.com/yourusername/portfolio-validator.git
cd portfolio-validator
```

2. Start the Spring Boot application:
```bash
./mvnw spring-boot:run
```

3. Open your browser and go to `http://localhost:8080/swagger-ui.html` to test the API!

## Example Payload

You can test the engine by sending this JSON to the `POST /api/v1/compliance/evaluate` endpoint:

```json
{
  "portfolioId": "PRT-12345",
  "baseCurrency": "USD",
  "holdings": [
    {
      "isin": "US0378331005",
      "name": "Apple Stock",
      "assetType": "EQUITY",
      "marketValueEur": 50000.00
    },
    {
      "isin": "USCASH000000",
      "name": "Cash Reserve",
      "assetType": "CASH",
      "marketValueEur": 15000.00
    }
  ]
}
```
