# Smart Currency Monitoring and Exchange System

## 📌 Project Overview
The **Smart Currency Monitoring and Exchange System** is a full-stack web application designed to facilitate secure, real-time currency exchange operations. It provides users with dynamic currency conversion capabilities, a secure digital wallet system, and detailed transaction history, all wrapped in a modern, responsive user interface.

## 🚀 Key Features

### User & Authentication
- **Secure Registration & Login:** JWT-based stateless authentication.
- **Role-Based Access Control:** Differentiated access for standard Users and Administrators.

### Currency & Transactions
- **Real-time Currency Conversion:** Dynamic exchange rate calculation (`Converted Amount = (Amount - 1% Fee) × (ToRate / FromRate)`).
- **Wallet Management:** Automatic wallet creation and balance updates during transactions.
- **Transaction History & Filtering:** View and filter past transactions by date and currency pairs.
- **Daily Volume Limit:** Limits users to 10,000 equivalent units per day to ensure security and prevent abuse.

### Dashboard & UI
- **Modern User Interface:** Built with Tailwind CSS for a highly responsive, professional design.
- **Admin Dashboard:** Centralized view for administrators to monitor platform activity, update currency rates, and oversee users.

### Security & Integrity
- **Robust Input Validation:** Ensures data integrity using Spring Boot Validation (JSR-303).
- **Transactional Consistency:** `@Transactional` ensures atomic database updates to prevent financial discrepancies.

## 🛠️ Technology Stack
- **Backend:** Java 17, Spring Boot (Web, Data JPA, Security, Validation)
- **Database:** MySQL
- **Authentication:** JSON Web Tokens (JWT)
- **Frontend:** HTML5, Vanilla JavaScript, Tailwind CSS

## 📋 Prerequisites
- Java 17+
- Maven 3.8+
- MySQL Server 8.0+

## ⚙️ Local Setup and Installation

1. **Clone the repository:**
   ```bash
   git clone https://github.com/mutoniangel/Smart-Currency-monitoring-and-exchange-system.git
   cd Smart-Currency-monitoring-and-exchange-system/smart_currency
   ```

2. **Configure Database:**
   - Create a MySQL database named `smart_currency`.
   - Update `src/main/resources/application.properties` with your MySQL credentials:
     ```properties
     spring.datasource.url=jdbc:mysql://localhost:3306/smart_currency
     spring.datasource.username=your_mysql_username
     spring.datasource.password=your_mysql_password
     ```
   - (Optional) Import the initial database schema using the `database.sql` file provided in the repository.

3. **Build and Run the Application:**
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

4. **Access the Application:**
   - Open your browser and navigate to: `http://localhost:8080` (or `8081` depending on your configuration).
   - You will be greeted by the `index.html` landing page.

## 🔌 Core API Endpoints

- `POST /api/auth/register` - Register a new user
- `POST /api/auth/login` - Authenticate a user and receive a JWT
- `GET /api/currencies/filter` - Fetch and filter available currencies
- `POST /api/transactions` - Process a new currency exchange
- `GET /api/transactions/filter` - View transaction history with filters

## 🛡️ Business Rules & Logic
- A user cannot perform an exchange without sufficient balance.
- Exchange rate calculations strictly enforce a dynamic fee and ensure the source and destination currencies differ.
- Transactions are permanently recorded (No delete endpoints).
- Daily transactions are capped at 10,000 equivalent units.

## 🤝 Contributing
Contributions, issues, and feature requests are welcome! Feel free to check the issues page if you want to contribute.

## 📝 License
This project is open-source and available under the [MIT License](LICENSE).
