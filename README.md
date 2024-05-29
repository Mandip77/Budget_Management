# SuperBudget

SuperBudget is a Spring Boot application designed to help users manage budget categories and transactions. This project is built with Java, Spring Boot, JPA, MySQL, and Docker.

## Table of Contents
- [Features](#features)
- [Prerequisites](#prerequisites)
- [Setup](#setup)
   - [Local Setup](#local-setup)
   - [Docker Setup](#docker-setup)
- [Usage](#usage)
- [Contributing](#contributing)
- [License](#license)
- [Contact](#contact)

## Features
- Manage budget categories
- Track transactions
- RESTful API for interacting with budget data
- Dockerized for easy setup and deployment

## Prerequisites
- Java JDK 11 or higher
- Maven
- Docker
- MySQL

## Setup

### Local Setup
Follow these steps to set up the project locally:

1. **Clone the repository**:
   
         git clone https://github.com/your-username/SuperBudget.git
         cd SuperBudget

2. **Set up the database**
   
         mysql -u root -p < setup.sql

3. **Update application.properties**:

   Update the src/main/resources/application.properties file with your MySQL credentials:
   
         spring.datasource.url=jdbc:mysql://localhost:3306/budgetapp
         spring.datasource.username=newuser
         spring.datasource.password=your_password
         spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
         spring.jpa.hibernate.ddl-auto=update
         spring.jpa.show-sql=true
         spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL5Dialect

4. **Build the project**:
      
         mvn clean package

5. **Run the application**:

       java -jar target/SuperBudget-0.0.1-SNAPSHOT.jar

Access the application:
    Open your web browser and go to http://localhost:8080.

## Docker Setup

Follow these steps to set up the project using Docker:

   1. Build and run the application using Docker Compose:

    mvn clean package
    docker-compose up --build

   2. Access the application:
    Open your web browser and go to http://localhost:8080.

## Usage

You can interact with the application through its RESTful API. Here are some example endpoints:

Get all budget categories:

      GET /api/budget-categories

Create a new budget category:
      
      POST /api/budget-categories
      {
      "name": "Groceries",
      "allocation": 500.00,
      "balance": 500.00
      }

Get all transactions:

      GET /api/transactions

Create a new transaction:

    POST /api/transactions
    {
        "description": "Milk",
        "amount": 5.00,
        "budgetCategoryId": 1
    }

Contributing

First off, thanks for taking the time to contribute! 🎉

Please read our Contributing Guidelines for details on our code of conduct, and the process for submitting pull requests to us.
License

This project is licensed under the MIT License - see the LICENSE file for details.
Contact

If you have any questions or feedback, feel free to reach out to us:

    Email: mandip.amgain123@gmail.com
    GitHub: Mandip77
