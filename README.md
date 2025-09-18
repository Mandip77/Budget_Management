<p align="center">
  <img src="https://fonts.gstatic.com/s/e/notoemoji/15.0/1f4b8/72.png" alt="money emoji" width="60"/>
</p>

<h1 align="center">
  💸✨ SuperBudget ✨💸<br>
  <sub>Personal Budget Management System</sub>
</h1>

<p align="center">
  <b>Take charge of your finances—with a smile!</b><br>
  <i>Track expenses, manage categories, and reach your goals with ease.</i>
</p>

---

<div align="center">
  <img src="https://readme-typing-svg.demolab.com?font=Fira+Code&pause=800&color=34B7F1&center=true&width=435&lines=Welcome+to+SuperBudget!;Budgeting+made+fun+%26+easy!;Let's+start+saving+%F0%9F%98%8E" alt="Typing SVG" />
</div>

---

## 📚 Table of Contents

- [✨ Features](#-features)
- [🛠️ Tech Stack](#-tech-stack)
- [⚡ Quick Start](#-quick-start)
    - [🔧 Local Setup](#-local-setup)
- [💡 App Features (Overview)](#-app-features-overview)
- [🌐 Web Endpoints](#-web-endpoints)
- [🗄️ Database Schema](#-database-schema)
- [🪛 Troubleshooting](#-troubleshooting)
- [🧪 Sample Data](#-sample-data)
- [🤝 Contributing](#-contributing)
- [📄 License](#-license)
- [📬 Contact](#-contact)

---

## ✨ Features

<div align="center">
  <img src="https://media.giphy.com/media/3o7aD7l9MhpQwM6NYY/giphy.gif" width="200" />
</div>

### 💵 Budget Management
- **Create & Edit Categories:** Groceries, Entertainment, Transportation, and more!
- **Set Monthly Allocations:** Define your spending limits.
- **Real-Time Balance:** Instantly see your remaining balance.
- **Automatic Adjustments:** Edit category names and allocations—balances update automatically!

### 🧾 Transaction Magic
- **Add, Edit, Delete Transactions:** Quick and simple.
- **Budget Validation:** Prevents overspending (we got your back!).
- **Automatic Balance Restoration:** Delete a transaction? Your budget is instantly corrected.

### 📊 Dashboard & Reporting
- **All-in-One Dashboard:** View categories and transactions at a glance.
- **Live Updates:** Balances and activities update in real time.
- **Visual Alerts:** Color-coded highlights for overspending and low balances.
- **Fully Responsive:** Looks great on any device! 📱💻

---

## 🛠️ Tech Stack

| Layer      | Technology                                      |
|------------|-------------------------------------------------|
| Backend    | Spring Boot 3.x, Spring Data JPA, Spring MVC    |
| Frontend   | Thymeleaf, Bootstrap 4.5, jQuery                |
| Database   | MySQL 8.0                                       |
| Build Tool | Maven                                           |
| Java       | OpenJDK 17                                      |
| 🐳 Docker  | *Support coming soon!*                          |

---

## ⚡ Quick Start

> **🎉 Docker support is coming soon! For now, let's get started locally:**

### 🔧 Local Setup

1. **Clone the Repo**
    ```bash
    git clone https://github.com/Mandip77/SuperBudget.git
    cd SuperBudget
    ```

2. **Set Up MySQL Database**
    ```bash
    mysql -u root -p < setup.sql
    ```

3. **Configure Database Connection**
    Open `src/main/resources/application.properties` and update:
    ```properties
    spring.datasource.url=jdbc:mysql://localhost:3306/budgetapp?allowPublicKeyRetrieval=true&useSSL=false
    spring.datasource.username=newuser
    spring.datasource.password=newpassword
    ```

4. **Build & Run**
    ```bash
    mvn clean package
    java -jar target/SuperBudget-0.0.1-SNAPSHOT.jar
    ```

5. **Open in Browser**
    Visit [http://localhost:8080](http://localhost:8080)

---

## 💡 App Features (Overview)

<div align="center">
  <img src="https://media.giphy.com/media/xUOxfjsW6TqPF1nZ8k/giphy.gif" width="200"/>
</div>

### 🏠 Dashboard
- **Budget Categories Table:** Names, allocations, balances, and activity
- **Recent Transactions Table:** Descriptions, amounts, categories
- **Quick Add Forms:** Easily add transactions and categories

### 📁 Budget Category Management
- **Create, Edit, Delete:** Add new, modify, or remove categories
- **Balance Tracking:** Always up-to-date with your spending

### 📝 Transaction Management
- **Add, Edit, Delete:** Full CRUD for your expenses
- **Validation:** No more overspending mistakes

### 🧙‍♂️ Smart Features
- **Overspending Protection:** Can’t go over your budget!
- **Automatic Calculations:** Everything updates in real time.
- **Data Integrity:** Safe and reliable, with smart deletes.
- **Friendly Errors:** Clear messages guide you.

---

## 🌐 Web Endpoints

| Endpoint                   | Method | Description                               |
|----------------------------|--------|-------------------------------------------|
| `/`                        | GET    | Main dashboard                            |
| `/add-transaction`         | POST   | Add a new transaction                     |
| `/edit-transaction/{id}`   | GET    | Show transaction edit form                |
| `/edit-transaction`        | POST   | Update transaction                        |
| `/delete-transaction/{id}` | GET    | Delete a transaction                      |
| `/add-category`            | POST   | Add a new budget category                 |
| `/edit-category/{id}`      | GET    | Show category edit form                   |
| `/edit-category`           | POST   | Update category                           |
| `/delete-category/{id}`    | GET    | Delete a category                         |

---

## 🗄️ Database Schema

<details>
  <summary><b>Click to view SQL schema!</b></summary>

### `budget_category`
```sql
id BIGINT PRIMARY KEY AUTO_INCREMENT,
name VARCHAR(255) NOT NULL,
allocation DECIMAL(10,2) NOT NULL,
balance DECIMAL(10,2) NOT NULL,
activity DECIMAL(10,2) DEFAULT 0.0,
remaining_amount DECIMAL(10,2) DEFAULT 0.0
```

### `transaction`
```sql
id BIGINT PRIMARY KEY AUTO_INCREMENT,
description VARCHAR(255) NOT NULL,
amount DECIMAL(10,2) NOT NULL,
budget_category_id BIGINT NOT NULL,
FOREIGN KEY (budget_category_id) REFERENCES budget_category(id)
```
</details>

---

## 🪛 Troubleshooting

<div align="center">
  <img src="https://media.giphy.com/media/3ohs7KViFvUlXhG5lC/giphy.gif" width="120"/>
</div>

### 🐬 Database Issues
- **Access denied?** Double-check your MySQL credentials.
- **Timeouts?** Make sure MySQL is running and accessible.
- **Schema errors?** Run `setup.sql` to initialize the database.

### ⚙️ Application Issues
- **Validation errors?** Check your category balances before adding transactions.
- **Missing categories?** Add a budget category before transactions.

### 🛠️ Common Fix
```bash
mvn clean package -DskipTests
```

---

## 🧪 Sample Data

Sample categories included:
- 🥦 **Groceries:** $500.00
- 🎬 **Entertainment:** $200.00
- 🚗 **Transportation:** $300.00

---

## 🤝 Contributing

<div align="center">
  <img src="https://media.giphy.com/media/l0MYQbT7p8Bz9c2mY/giphy.gif" width="120"/>
</div>

1. **Fork** this repo
2. **Create a branch:**  
   `git checkout -b feature/my-feature`
3. **Commit:**  
   `git commit -am 'Add a new feature'`
4. **Push:**  
   `git push origin feature/my-feature`
5. **Open a Pull Request!**

---

## 📄 License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

---

## 📬 Contact

- **Email:** mandip.amgain123@gmail.com  
- **GitHub:** [Mandip77](https://github.com/Mandip77)  
- **Project Repo:** [SuperBudget](https://github.com/Mandip77/SuperBudget)

---

<p align="center">
  <b>🚀 SuperBudget: Budgeting made joyful! 🚀</b><br>
  <i>For personal use and learning. For production, add extra security and backups!</i>
</p>
