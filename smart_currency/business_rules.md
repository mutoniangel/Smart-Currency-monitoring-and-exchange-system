# Advanced Features & Business Logic Documentation
## Smart Currency Monitoring and Exchange System

### 1. Introduction
This phase focuses on implementing advanced backend logic and business rules to enhance the functionality, reliability, and usability of the Smart Currency Monitoring and Exchange System.
The system was improved by adding data validation, filtering mechanisms, workflow processing, and intelligent transaction handling to ensure accuracy and security in currency exchange operations.

### 2. Objectives
The objectives of this phase include:
- Implement input validation
- Apply business rules in transaction processing
- Introduce filtering and search functionality
- Improve workflow logic
- Ensure data consistency and integrity

### 3. Input Validation
Validation ensures that only correct and meaningful data is accepted by the system.
**Implemented Validations:**
- **User Registration**
  - Username must not be empty (`@NotBlank`)
  - Email must be valid format (`@Email`)
  - Password must meet minimum length (`@Size(min=6)`)
- **Currency Input**
  - Currency code must exist
  - Exchange rate must be positive (`@DecimalMin(value="0.0001")`)
- **Transaction Validation**
  - Amount must be greater than 0 (`@DecimalMin("0.01")`)
  - User must have sufficient balance
  - Source and destination currencies must be different

> [!NOTE]
> 📸 **Screenshot Placeholder**: (Add validation error responses from Postman showing 400 Bad Request with field errors)

### 4. Business Rules Implementation
Business rules define how the system behaves in real-world scenarios.
**Key Business Rules:**
1. A user cannot perform a transaction without logging in (Spring Security & JWT)
2. A user cannot exchange currency if balance is insufficient
3. Exchange rate must be applied correctly during conversion (calculated as `rate = toCurr / fromCurr`)
4. Every transaction must be recorded with a status (`PENDING`, `COMPLETED`, `FAILED`)
5. Admin only can update currency rates
6. Transactions cannot be deleted (no delete endpoint exists, ensuring financial integrity)
7. **Daily Volume Limit**: Users are limited to 10,000 equivalent units per day to prevent abuse.

### 5. Currency Conversion Logic
The system performs conversion using the formula:
`Converted Amount = (Amount - 1% Fee) × (ToCurrencyRate / FromCurrencyRate)`

**Workflow:**
1. User selects currencies
2. System retrieves current rate
3. Conversion is calculated
4. Wallet balance is updated
5. Transaction is saved with `COMPLETED` status

> [!NOTE]
> 📸 **Screenshot Placeholder**: (Add conversion request and response showing a successful exchange)

### 6. Wallet Processing Logic
The wallet system ensures proper handling of user balances.
**Features:**
- Deduct balance when exchanging
- Add converted amount to target wallet (create wallet if not exists)
- Prevent negative balance (Strictly enforced in `TransactionService`)
- Check sufficient funds before transaction

> [!NOTE]
> 📸 **Screenshot Placeholder**: (Add wallet update example showing balance changes)

### 7. Filtering & Search Features
Filtering improves usability by allowing users to find specific data quickly.
**Implemented Filters:**
- **Transaction Filtering** (`GET /api/transactions/filter`)
  - By date (`startDate`, `endDate`)
  - By target currency (`targetCurrency`)
- **Currency Filtering** (`GET /api/currencies/filter`)
  - Search by currency code (`code`)
  - Filter by trend (`trend`: UP/DOWN/STABLE)

> [!NOTE]
> 📸 **Screenshot Placeholder**: (Add filtered API results from `/api/transactions/filter` and `/api/currencies/filter`)

### 8. Workflow Processing
The system follows a structured workflow for each operation.
**Transaction Workflow:**
1. User logs in (JWT)
2. Selects currency pair
3. Enters amount
4. System validates input (JSR-303)
5. System checks daily limit (10,000 units)
6. System checks balance
7. Conversion is processed
8. Transaction is saved (Audit Log)
9. Response is returned

### 10. Performance Improvements
- **Optimized JPA Queries**: Used custom `@Query` with parameter mapping.
- **Stateless Authentication**: JWT reduces server-side session overhead.
- **Transactional Integrity**: `@Transactional` ensures atomic updates to wallets and history.

### 11. GitHub Repository Update
All advanced features and business logic have been implemented and pushed to the GitHub repository.

**Repository Link:**
[https://github.com/mutoniangel/Smart-Currency-monitoring-and-exchange-system](https://github.com/mutoniangel/Smart-Currency-monitoring-and-exchange-system)

### 12. Testing of Features
All features were tested using Postman.
**Tested Scenarios:**
- Valid transaction -> Success
- Invalid transaction (Negative amount) -> Validation Error
- Insufficient balance -> "Insufficient balance" error
- Filtering requests -> Correct subsets returned
- Admin actions -> Sync and Rate Updates functional

> [!NOTE]
> 📸 **Screenshot Placeholder**: (Add Postman test screenshots for each scenario)

### 13. Conclusion
This phase successfully enhanced the system by implementing advanced business logic and features. The system now ensures proper validation, secure transaction handling, efficient workflows, and improved user experience. These improvements make the system more realistic, reliable, and ready for real-world application.
