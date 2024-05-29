# Contributing to SuperBudget

First off, thanks for taking the time to contribute! 🎉

The following is a set of guidelines for contributing to SuperBudget, a Spring Boot application for managing budget categories and transactions.

## Code of Conduct

This project and everyone participating in it is governed by the [Code of Conduct](CODE_OF_CONDUCT.md). By participating, you are expected to uphold this code.

## How Can I Contribute?

### Reporting Bugs

If you find a bug, please create an issue and provide detailed information to help us reproduce and fix the bug. Include the following:
- Steps to reproduce the issue
- Expected behavior
- Actual behavior
- Screenshots (if applicable)
- Environment (OS, browser, version, etc.)

### Suggesting Enhancements

If you have an idea for an enhancement, please create an issue and describe your suggestion in detail. Explain why this feature would be useful and how it should work.

### Submitting Changes

1. **Fork the repository** to your own GitHub account.
2. **Clone the project** to your local machine:
  
         git clone https://github.com/your-username/SuperBudget.git

3. **Create a new branch for your feature or bug fix**:

         git checkout -b feature-or-bugfix-name

4. **Make your changes. Ensure your code adheres to the project's coding standards**.
5. **Commit your changes with a clear commit message**:

         git commit -m "Description of your changes"

6. **Push to your branch**:

       git push origin feature-or-bugfix-name

7. **Open a Pull Request on GitHub and provide a detailed description of your changes**.

## Development Setup

1. **Prerequisites**:
   
        Java JDK 11 or higher
        Maven
        Docker
        MySQL

2. **Clone the repository**:
   
         git clone https://github.com/your-username/SuperBudget.git
         cd SuperBudget

3. **Set up the database**:

      mysql -u root -p < setup.sql

4. **Build the project**:

         mvn clean package

5. **Run the application**:


       docker-compose up --build

6. **Access the application at http://localhost:8080**.

### Coding Standards

    Follow the existing code style in the project.
    Write clear and concise commit messages.
    Write unit tests for your code.

### Pull Request Process

    Ensure your code follows the coding standards and passes all tests.
    Describe the changes in your pull request.
    Link to any relevant issues in the pull request description.
    A project maintainer will review your pull request and provide feedback.

### License

By contributing to SuperBudget, you agree that your contributions will be licensed under the MIT License.
