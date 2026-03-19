-- Smart Currency Monitoring and Exchange System Database Script
-- This script creates the database schema and inserts initial seed data.

CREATE DATABASE IF NOT EXISTS smart_currency;
USE smart_currency;

-- 1. Admins Table
CREATE TABLE IF NOT EXISTS Admin (
    adminID INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE
);

-- 2. Users Table
CREATE TABLE IF NOT EXISTS Users (
    userID INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    role VARCHAR(20) NOT NULL DEFAULT 'USER'
);

-- 3. Currencies Table
CREATE TABLE IF NOT EXISTS Currency (
    currencyCode VARCHAR(10) PRIMARY KEY,
    currencyName VARCHAR(50) NOT NULL,
    currentRate DECIMAL(10, 4) NOT NULL,
    trend VARCHAR(20),
    AdminID INT,
    FOREIGN KEY (AdminID) REFERENCES Admin(adminID)
);

-- 4. Wallets Table
CREATE TABLE IF NOT EXISTS Wallet (
    walletID INT AUTO_INCREMENT PRIMARY KEY,
    UserID INT NOT NULL,
    CurrencyCode VARCHAR(10) NOT NULL,
    balance DECIMAL(18, 4) DEFAULT 0.0000,
    FOREIGN KEY (UserID) REFERENCES Users(userID),
    FOREIGN KEY (CurrencyCode) REFERENCES Currency(currencyCode)
);

-- 5. Transactions Table
CREATE TABLE IF NOT EXISTS Transaction (
    transactionID INT AUTO_INCREMENT PRIMARY KEY,
    UserID INT NOT NULL,
    FromCurrencyCode VARCHAR(10) NOT NULL,
    ToCurrencyCode VARCHAR(10) NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    exchangeRate DECIMAL(10, 4) NOT NULL,
    transactionDate DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (UserID) REFERENCES Users(userID),
    FOREIGN KEY (FromCurrencyCode) REFERENCES Currency(currencyCode),
    FOREIGN KEY (ToCurrencyCode) REFERENCES Currency(currencyCode)
);

-- SEED DATA (Optional)
INSERT IGNORE INTO Admin (username, password, email) VALUES ('admin', '$2a$10$YourEncodedPassword', 'admin@smartcurrency.com');

INSERT IGNORE INTO Currency (currencyCode, currencyName, currentRate, trend, AdminID) VALUES 
('USD', 'United States Dollar', 1.0000, 'STABLE', 1),
('EUR', 'Euro', 0.9200, 'UP', 1),
('GBP', 'British Pound', 0.7800, 'DOWN', 1),
('JPY', 'Japanese Yen', 150.2500, 'STABLE', 1);

-- Note: Password in seed data should be BCrypt encoded if using Spring Security.
