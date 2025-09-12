FROM maven:3.8.3-openjdk-17
WORKDIR /app
COPY . .
RUN mvn clean install -DskipTests
CMD sh -c "mvn test && mvn allure:report && mvn allure:serve"
EXPOSE 8080