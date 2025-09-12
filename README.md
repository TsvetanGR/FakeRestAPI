# Automated Tests for Fake Rest API
This project is a testing framework based on **Java** using **JUnit**, **HTTP Client**, and **Allure**.  
It contains automated tests for a fake REST API.

## Tools & Libraries Used
- **JUnit**: Test execution and management
- **HTTP Client**: Sending requests and validating responses
- **Allure**: Generating test reports

## Project Structure
.github/workflows/ # GitHub Actions workflows (CI/CD pipeline)
src/main/java/configs/ # Configuration utilities (ConfigLoader)
src/main/java/core/http # Core HTTP client and response handling
src/main/java/model/ # POJOs / Data models (Authors, Books, Errors)
src/main/java/services/ # Service classes (AuthorsService, BooksService)
src/main/resources/ # Project-level configuration
src/test/java/com/httpclient/ # Test classes (AuthorsTests, BookTests)
src/test/resources/ # Allure reporting configuration
docker-compose.yml # Docker setup (if services are containerized)
Dockerfile # Build definition for Docker container
pom.xml # Maven dependencies & build config
.gitignore # Ignored files for Git


## Prerequisites
- **Java**: Version 17
- **Maven**: For project build and dependency management
- **Allure**: For generating test reports  
This project uses Maven as the build tool.
## Running the Tests
To execute the tests locally, run:

## mvn clean test
Generate Allure Report:

## mvn allure:report
The report is generated in the Actions tab.