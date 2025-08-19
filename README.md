# SuperBudget - Personal Budget Management System

SuperBudget is a comprehensive Spring Boot web application designed to help users manage their personal finances through budget categories and transaction tracking. Built with modern web technologies and containerized for easy deployment.

## 📋 Table of Contents
- [Features](#features)
- [Technology Stack](#technology-stack)
- [Prerequisites](#prerequisites)
- [Quick Start](#quick-start)
- [Setup Methods](#setup-methods)
    - [Docker Setup (Recommended)](#docker-setup-recommended)
    - [Local Development Setup](#local-development-setup)
- [Application Features](#application-features)
- [API Endpoints](#api-endpoints)
- [Database Schema](#database-schema)
- [Troubleshooting](#troubleshooting)
- [Contributing](#contributing)
- [License](#license)

## ✨ Features

### Budget Management
- **Create Budget Categories**: Set up categories like Groceries, Entertainment, Transportation
- **Allocation Management**: Define monthly budget allocations for each category
- **Real-time Balance Tracking**: Monitor remaining balance and spending activity
- **Edit Categories**: Modify category names and allocations with automatic balance adjustments

### Transaction Management
- **Add Transactions**: Record expenses against specific budget categories
- **Edit Transactions**: Modify transaction amounts, descriptions, and categories
- **Delete Transactions**: Remove transactions with automatic balance restoration
- **Budget Validation**: Prevents overspending beyond allocated category limits

### Dashboard & Reporting
- **Interactive Dashboard**: View all budget categories and recent transactions
- **Real-time Updates**: Automatic balance calculations and activity tracking
- **Visual Indicators**: Color-coded alerts for overspending and low balances
- **Responsive Design**: Mobile-friendly interface with Bootstrap styling

## 🛠 Technology Stack

- **Backend**: Spring Boot 3.x, Spring Data JPA, Spring MVC
- **Frontend**: Thymeleaf, Bootstrap 4.5, jQuery
- **Database**: MySQL 8.0
- **Build Tool**: Maven
- **Containerization**: Docker & Docker Compose
- **Java Version**: OpenJDK 17

## 📋 Prerequisites

### For Docker Setup (Recommended)
- Docker Desktop or Docker Engine
- Docker Compose

### For Local Development
- Java JDK 17 or higher
- Maven 3.6+
- MySQL 8.0+
- Git

## 🚀 Quick Start

### Docker Setup (Recommended)

1. **Clone the repository**:
   ```bash
   git clone https://github.com/your-username/SuperBudget.git
   cd SuperBudget
   ```

2. **Build and run with Docker Compose**:
   ```bash
   # Build the Spring Boot application
   mvn clean package -DskipTests
   
   # Start all services
   docker-compose up --build
   ```

3. **Access the application**:
    - Open your browser and navigate to: http://localhost:8080
    - The database will be automatically initialized with sample data

4. **Stop the application**:
   ```bash
   docker-compose down
   ```

### Local Development Setup

1. **Clone and navigate to the repository**:
   ```bash
   git clone https://github.com/your-username/SuperBudget.git
   cd SuperBudget
   ```

2. **Set up MySQL database**:
   ```bash
   mysql -u root -p < setup.sql
   ```

3. **Configure database connection**:
   Update `src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/budgetapp?allowPublicKeyRetrieval=true&useSSL=false
   spring.datasource.username=newuser
   spring.datasource.password=newpassword
   ```

4. **Build and run**:
   ```bash
   mvn clean package
   java -jar target/SuperBudget-0.0.1-SNAPSHOT.jar
   ```

5. **Access the application**:
   Open http://localhost:8080 in your browser

## 📱 Application Features

### Main Dashboard
- **Budget Categories Table**: Shows category name, allocation, current balance, and activity
- **Recent Transactions Table**: Displays transaction descriptions, amounts, and associated categories
- **Add Transaction Form**: Quick transaction entry with category selection
- **Add Category Form**: Create new budget categories with initial allocations

### Budget Category Management
- **Create**: Add new budget categories with custom allocations
- **Edit**: Modify category details with automatic balance recalculation
- **Delete**: Remove categories (cascades to associated transactions)
- **Balance Tracking**: Real-time balance updates based on transactions

### Transaction Management
- **Add**: Create transactions against specific budget categories
- **Edit**: Modify transaction details including changing categories
- **Delete**: Remove transactions with automatic budget restoration
- **Validation**: Prevents transactions that exceed category budgets

### Smart Features
- **Overspending Protection**: Blocks transactions that would exceed category limits
- **Automatic Calculations**: Real-time balance and activity updates
- **Data Integrity**: Cascade deletes and referential integrity
- **User-Friendly Errors**: Clear error messages for invalid operations

## 🔌 Web Interface Endpoints

| Endpoint | Method | Description |
|----------|---------|-------------|
| `/` | GET | Main dashboard with categories and transactions |
| `/add-transaction` | POST | Create a new transaction |
| `/edit-transaction/{id}` | GET | Edit transaction form |
| `/edit-transaction` | POST | Update transaction |
| `/delete-transaction/{id}` | GET | Delete a transaction |
| `/add-category` | POST | Create a new budget category |
| `/edit-category/{id}` | GET | Edit category form |
| `/edit-category` | POST | Update category |
| `/delete-category/{id}` | GET | Delete a category |

## 🗄 Database Schema

### Budget Categories Table
```sql
budget_category (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    allocation DECIMAL(10,2) NOT NULL,
    balance DECIMAL(10,2) NOT NULL,
    activity DECIMAL(10,2) DEFAULT 0.0,
    remaining_amount DECIMAL(10,2) DEFAULT 0.0
)
```

### Transactions Table
```sql
transaction (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    description VARCHAR(255) NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    budget_category_id BIGINT NOT NULL,
    FOREIGN KEY (budget_category_id) REFERENCES budget_category(id)
)
```

## 🔧 Configuration Files

### Docker Configuration
- `docker-compose.yml`: Multi-container setup with MySQL and Spring Boot
- `Dockerfile`: Spring Boot application containerization
- `setup.sql`: Database initialization script

### Application Configuration
- `application.properties`: Default local configuration
- `application-docker.properties`: Docker-specific database connection
- `pom.xml`: Maven dependencies and build configuration

## 🚨 Troubleshooting

### Docker Issues
- **Connection refused**: Ensure Docker Desktop is running
- **Port conflicts**: Check if port 8080 or 3306 are already in use
- **Build failures**: Run `mvn clean package` before `docker-compose up`

### Database Connection Issues
- **Access denied**: Verify MySQL credentials in configuration files
- **Connection timeout**: Ensure MySQL service is running and accessible
- **Schema errors**: Run the setup.sql script to initialize the database

### Application Errors
- **Transaction validation errors**: Check category balance before adding transactions
- **Missing categories**: Ensure at least one budget category exists before adding transactions

### Common Solutions
```bash
# Reset Docker environment
docker-compose down -v
docker-compose up --build

# Check container logs
docker-compose logs app
docker-compose logs db

# Rebuild application
mvn clean package -DskipTests
```

## 🧪 Sample Data

The application includes sample budget categories:
- **Groceries**: $500.00 allocation
- **Entertainment**: $200.00 allocation
- **Transportation**: $300.00 allocation

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/new-feature`)
3. Commit your changes (`git commit -am 'Add some feature'`)
4. Push to the branch (`git push origin feature/new-feature`)
5. Create a new Pull Request

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 📞 Contact

- **Email**: mandip.amgain123@gmail.com
- **GitHub**: [Mandip77](https://github.com/Mandip77)
- **Project Repository**: [SuperBudget](https://github.com/Mandip77/SuperBudget)

---

**Note**: This application is designed for personal budget management and educational purposes. For production use, consider additional security measures and data backup strategies.