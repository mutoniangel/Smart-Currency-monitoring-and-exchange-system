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
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    role VARCHAR(20) NOT NULL DEFAULT 'USER',
    enabled BOOLEAN NOT NULL DEFAULT TRUE
);

-- 3. Currencies Table
CREATE TABLE IF NOT EXISTS Currency (
    currency_code VARCHAR(10) PRIMARY KEY,
    currency_name VARCHAR(50) NOT NULL,
    current_rate DECIMAL(10, 4) NOT NULL,
    trend VARCHAR(20),
    admin_id INT,
    FOREIGN KEY (admin_id) REFERENCES Admin(adminID)
);

-- 4. Wallets Table
CREATE TABLE IF NOT EXISTS Wallet (
    wallet_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    currency_code VARCHAR(10) NOT NULL,
    balance DECIMAL(18, 4) DEFAULT 0.0000,
    FOREIGN KEY (user_id) REFERENCES Users(user_id),
    FOREIGN KEY (currency_code) REFERENCES Currency(currency_code)
);

-- 5. Transactions Table
CREATE TABLE IF NOT EXISTS Transaction (
    transaction_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    from_currency_code VARCHAR(10) NOT NULL,
    to_currency_code VARCHAR(10) NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    exchange_rate DECIMAL(10, 4) NOT NULL,
    fee DECIMAL(10, 4),
    converted_amount DECIMAL(18, 4),
    base_amount DECIMAL(18, 4),
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    transaction_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES Users(user_id),
    FOREIGN KEY (from_currency_code) REFERENCES Currency(currency_code),
    FOREIGN KEY (to_currency_code) REFERENCES Currency(currency_code)
);

-- SEED DATA
INSERT IGNORE INTO Admin (username, password, email) VALUES ('admin', '$2a$10$YourEncodedPassword', 'admin@smartcurrency.com');

INSERT IGNORE INTO Currency (currency_code, currency_name, current_rate, trend, admin_id) VALUES 
('USD', 'US Dollar', 1.0000, 'STABLE', 1),
('EUR', 'Euro', 0.9200, 'UP', 1),
('GBP', 'British Pound', 0.7800, 'DOWN', 1),
('JPY', 'Japanese Yen', 150.2500, 'STABLE', 1),
('RWF', 'Rwandan Franc', 1200.0000, 'STABLE', 1),
('UGX', 'Uganda Shillings', 3800.0000, 'STABLE', 1),
('KES', 'Kenyan Shillings', 130.0000, 'STABLE', 1),
('TZS', 'Tanzanian Shillings', 2500.0000, 'STABLE', 1),
('CAD', 'Canadian Dollar', 1.3500, 'STABLE', 1),
('AUD', 'Australian Dollar', 1.5200, 'STABLE', 1),
('CHF', 'Swiss Franc', 0.8800, 'STABLE', 1),
('CNY', 'Chinese Yuan', 7.1900, 'STABLE', 1),
('INR', 'Indian Rupee', 82.9000, 'STABLE', 1),
('ZAR', 'South African Rand', 18.9500, 'STABLE', 1),
('AED', 'UAE Dirham', 3.6700, 'STABLE', 1),
('SAR', 'Saudi Riyal', 3.7500, 'STABLE', 1),
('TRY', 'Turkish Lira', 32.1000, 'STABLE', 1),
('BRL', 'Brazilian Real', 4.9800, 'STABLE', 1),
('MXN', 'Mexican Peso', 16.8000, 'STABLE', 1),
('NGN', 'Nigerian Naira', 1550.0000, 'STABLE', 1);

-- Note: Password in seed data should be BCrypt encoded if using Spring Security.
-- The DataInitializer.java handles proper seeding with encoded passwords on app startup.
