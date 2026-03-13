-- Database Export for Smart Currency Exchange System

-- 1. Users Table
CREATE TABLE Users (
    UserID INT AUTO_INCREMENT PRIMARY KEY,
    Username VARCHAR(50) NOT NULL UNIQUE,
    Password VARCHAR(255) NOT NULL,
    Email VARCHAR(100) NOT NULL UNIQUE,
    Role VARCHAR(20) NOT NULL
);

-- 2. Admin Table
CREATE TABLE Admin (
    AdminID INT AUTO_INCREMENT PRIMARY KEY,
    Username VARCHAR(50) NOT NULL UNIQUE,
    Password VARCHAR(255) NOT NULL,
    Email VARCHAR(100) NOT NULL UNIQUE
);

-- 3. Currency Table
CREATE TABLE Currency (
    CurrencyCode VARCHAR(10) PRIMARY KEY,
    CurrencyName VARCHAR(50) NOT NULL,
    CurrentRate DECIMAL(10,4) NOT NULL,
    Trend VARCHAR(20),
    AdminID INT,
    FOREIGN KEY (AdminID) REFERENCES Admin(AdminID)
);

-- 4. Transaction Table
CREATE TABLE Transaction (
    TransactionID INT AUTO_INCREMENT PRIMARY KEY,
    UserID INT,
    FromCurrencyCode VARCHAR(10),
    ToCurrencyCode VARCHAR(10),
    Amount DECIMAL(10,2),
    ExchangeRate DECIMAL(10,4),
    TransactionDate DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (UserID) REFERENCES Users(UserID),
    FOREIGN KEY (FromCurrencyCode) REFERENCES Currency(CurrencyCode),
    FOREIGN KEY (ToCurrencyCode) REFERENCES Currency(CurrencyCode)
);
 
-- 5. Wallet Table
CREATE TABLE Wallet (
    WalletID INT AUTO_INCREMENT PRIMARY KEY,
    UserID INT NOT NULL,
    CurrencyCode VARCHAR(10) NOT NULL,
    Balance DECIMAL(18,4) NOT NULL DEFAULT 0,
    FOREIGN KEY (UserID) REFERENCES Users(UserID),
    FOREIGN KEY (CurrencyCode) REFERENCES Currency(CurrencyCode)
);

-- Initial Data
INSERT INTO Admin (Username, Password, Email) VALUES ('admin', 'admin123', 'admin@smartcurrency.com');
INSERT INTO Currency (CurrencyCode, CurrencyName, CurrentRate, Trend, AdminID) VALUES ('USD', 'US Dollar', 1.0000, 'Stable', 1);
INSERT INTO Currency (CurrencyCode, CurrencyName, CurrentRate, Trend, AdminID) VALUES ('EUR', 'Euro', 0.9200, 'Up', 1);
INSERT INTO Users (Username, Password, Email, Role) VALUES ('john_doe', 'pass123', 'john@example.com', 'Customer');
