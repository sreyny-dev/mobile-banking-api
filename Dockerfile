# PHASE 1 - Download & Install JDK 21
FROM ghcr.io/graalvm/jdk-community:21
WORKDIR app
ADD ./build/libs/Mobile-Banking-1.0.jar /app/
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/Mobile-Banking-1.0.jar"]
