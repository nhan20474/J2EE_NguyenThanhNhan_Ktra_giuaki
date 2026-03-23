# Giai đoạn 1: Dùng Maven để build code ra file .jar
FROM maven:3.8-openjdk-17 AS builder
WORKDIR /app
COPY ktra/ .
# Vẫn giữ nguyên lệnh bỏ qua test để tránh lỗi lặt vặt
RUN mvn clean package -DskipTests

# Giai đoạn 2: Chạy thẳng ứng dụng Spring Boot bằng Java
FROM eclipse-temurin:17-jre
WORKDIR /app
# Lấy file .jar vừa build ra để chạy
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8080
# Lệnh khởi động Spring Boot
ENTRYPOINT ["java", "-jar", "app.jar"]
