CREATE DATABASE IF NOT EXISTS budgetapp;

CREATE USER IF NOT EXISTS 'newuser'@'localhost' IDENTIFIED BY 'your_password';
GRANT ALL PRIVILEGES ON budgetapp.* TO 'newuser'@'localhost';
FLUSH PRIVILEGES;

USE budgetapp;

CREATE TABLE IF NOT EXISTS budget_category (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    allocation DECIMAL(10, 2) NOT NULL,
    balance DECIMAL(10, 2) NOT NULL
);

CREATE TABLE IF NOT EXISTS transaction (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    description VARCHAR(255) NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    budget_category_id BIGINT NOT NULL,
    FOREIGN KEY (budget_category_id) REFERENCES budget_category(id)
);
