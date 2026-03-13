-- Initial Data for Smart Currency Exchange System
-- Note: Admin and Currency tables will be created by Hibernate

-- 1. Admin
INSERT INTO Admin (Username, Password, Email) VALUES ('admin', 'admin123', 'admin@smartcurrency.com');

-- 2. Currencies
INSERT INTO Currency (CurrencyCode, CurrencyName, CurrentRate, Trend, AdminID) VALUES ('USD', 'US Dollar', 1.0000, 'Stable', 1);
INSERT INTO Currency (CurrencyCode, CurrencyName, CurrentRate, Trend, AdminID) VALUES ('EUR', 'Euro', 0.9200, 'Up', 1);

-- 3. Users
INSERT INTO Users (Username, Password, Email, Role) VALUES ('john_doe', 'pass123', 'john@example.com', 'Customer');
