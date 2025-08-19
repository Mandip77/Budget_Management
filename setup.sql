create DATABASE IF NOT EXISTS budgetapp;

-- Create a user that can connect from other containers (not just localhost)
create USER IF NOT EXISTS 'newuser'@'%' IDENTIFIED BY 'newpassword';
GRANT ALL PRIVILEGES ON budgetapp.* TO 'newuser'@'%';
FLUSH PRIVILEGES;

use budgetapp;

-- Consider quoting table names that collide with SQL keywords.
-- If your JPA maps to `transaction`, use backticks here:
create TABLE IF NOT EXISTS budget_category (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    allocation DECIMAL(10, 2) NOT NULL,
    balance DECIMAL(10, 2) NOT NULL,
    activity DECIMAL(10, 2) DEFAULT 0.0,
    remaining_amount DECIMAL(10, 2) DEFAULT 0.0
);

create TABLE IF NOT EXISTS `transaction` (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    description VARCHAR(255) NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    budget_category_id BIGINT NOT NULL,
    FOREIGN KEY (budget_category_id) REFERENCES budget_category(id)
);
