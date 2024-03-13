FROM openjdk:17-jdk-slim

# jar 檔搬進容器
# COPY ["./target/springdemo-0.0.1-SNAPSHOT.jar", "/app/"]
ADD target/springdemo-0.0.1-SNAPSHOT.jar app.jar

# 容器 PORT
EXPOSE 8080

# 容器啟動時要在 command line 執行的指令 (預設在根目錄下)
# 切換目錄
# WORKDIR /app
CMD ["java", "-jar", "app.jar"]


# 建 image
# podman image build -f Dockerfile -t springdemo_app

# Run
# podman run -d -p 8080:8080 localhost/springdemo_app:latest