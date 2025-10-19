-- MySQL DDL for Finance Tracker
CREATE TABLE IF NOT EXISTS users (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(100) NOT NULL,
  email VARCHAR(150) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS categories (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS budgets (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  month_year DATE NOT NULL,
  amount DECIMAL(10,2) NOT NULL,
  CONSTRAINT fk_budget_user FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS transactions (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  category_id BIGINT NOT NULL,
  amount DECIMAL(10,2) NOT NULL,
  txn_date DATE NOT NULL,
  description VARCHAR(255),
  CONSTRAINT fk_tx_user FOREIGN KEY (user_id) REFERENCES users(id),
  CONSTRAINT fk_tx_category FOREIGN KEY (category_id) REFERENCES categories(id)
);

-- Seed sample data
INSERT INTO users (name, email) VALUES ('Demo User', 'demo@budgetai.local');

INSERT INTO categories (name) VALUES ('Grocery'),('Gas'),('Entertainment'),('Netflix'),('Pharmacy'),('Utilities');

-- Assume id 1 is Demo User
INSERT INTO budgets (user_id, month_year, amount) VALUES (1, DATE_FORMAT(CURDATE(), '%Y-%m-01'), 3000.00);

INSERT INTO transactions (user_id, category_id, amount, txn_date, description) VALUES
 (1, 1, 78.29, CURDATE(), 'Grocery Store'),
 (1, 2, 45.00, CURDATE() - INTERVAL 1 DAY, 'Gas Station'),
 (1, 4, 15.99, CURDATE() - INTERVAL 2 DAY, 'Netflix'),
 (1, 5, 32.50, CURDATE() - INTERVAL 3 DAY, 'Pharmacy'),
 (1, 3, 250.00, CURDATE() - INTERVAL 5 DAY, 'Concert Tickets');










