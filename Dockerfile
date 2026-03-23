# Sử dụng môi trường máy chủ Tomcat (rất phổ biến cho J2EE/Java Web)
FROM tomcat:10.1-jdk17

# Chép toàn bộ file trong project của bạn vào thư mục chạy web của Tomcat
COPY . /usr/local/tomcat/webapps/ROOT/

# Mở cổng 8080 cho ứng dụng
EXPOSE 8080
