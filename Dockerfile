# Giai đoạn 1: Dùng Maven để build code
FROM maven:3.8-openjdk-17 AS builder
WORKDIR /app
COPY ktra/ .
RUN mvn clean package

# Giai đoạn 2: Lấy file .war vừa build xong bỏ vào Tomcat
FROM tomcat:10.1-jdk17
COPY --from=builder /app/target/*.war /usr/local/tomcat/webapps/ROOT.war
EXPOSE 8080
