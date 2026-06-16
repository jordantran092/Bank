# Bank App

A full-stack banking application demonstrating **clean architecture**, **comprehensive testing**, and **RESTful API design**.
<br><br>
Click here for back-end portion of source code: [repository](https://github.com/jordantran092/Bank-App-Spring-Boot-REST-API/)


<!--


https://gitfront.io/r/jordan-tran/VLsxq5nGQwP6/Bank-App-Spring-Boot-REST-API/


-->

## Tech Stack
- **Backend:** Spring Boot, Java, PostgreSQL
- **Frontend:** Native Android Stack (Java, XML UI layouts, Android SDK, Android Studio)
- **Testing:** JUnit 5, Mockito, Spring Test

## Key Features
- Account creation
- Deposit/Withdraw/Transfer transactions
- Transaction history statements
- Input validation & error handling
- Service layer for financial service business logic
- DTO pattern for data transfer

## Testing
- **Unit Tests:** ClientService, TransactionService (using Mockito mocks)
- **Controller Integration Tests:** HTTP layer with JSON response assertions (Full Spring context with MockMvc)
- **Service Integration Tests:** Service layer + database interaction (Full Spring context with MockMvc)
- **Coverage:** Deposit, Withdraw, Transfer, Account Creation, Statement Generation, Error Handling, Edge Cases

## API Endpoints
`GET /api/v1/bank/status` - Current status<br>
`POST /api/v1/bank/clients` - Create account<br>
`PATCH /api/v1/bank/clients/{name}` - Deposit/Withdraw<br>
`PATCH /api/v1/bank/clients/bulk` - Transfer funds between clients<br>
`GET /api/v1/bank/clients/{name}` - Account statement<br>
