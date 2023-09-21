# 使用JDK-8作为基础镜像
FROM openjdk:8-jdk-alpine

# 设置工作目录
WORKDIR /app

# 将项目的jar文件复制到容器的工作目录中
COPY ../target/sql-easy.jar app.jar

# 暴露应用程序的端口号
EXPOSE 8080

# 设置JVM参数，可以根据需要进行调整
ENV JAVA_OPTS="-Xmx512m -Xms256m"

# 启动容器时运行的命令
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
