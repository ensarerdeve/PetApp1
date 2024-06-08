# Temel image olarak OpenJDK 17 kullanıyoruz
FROM openjdk:17-jdk-slim

# Uygulamanın çalışacağı dizini oluşturuyoruz
WORKDIR /app

COPY chat-service1-firebase-adminsdk-hplj2-5cbc50ef5e.json /app
# Ortam değişkenlerini tanımla
ENV FIREBASE=/app/chat-service1-firebase-adminsdk-hplj2-5cbc50ef5e.json
ENV STORAGE=/app/other_directory

# Maven'in bağımlılıkları yüklerken cache kullanılmasını sağlamak için bu dosyaları kopyalıyoruz
COPY pom.xml .
COPY src ./src

# Maven'i kullanarak uygulamayı derliyoruz
RUN apt-get update && apt-get install -y maven
RUN mvn clean package -DskipTests

# Uygulama jar dosyasını kopyalıyoruz
COPY target/*.jar app.jar

# Uygulamayı başlatıyoruz
ENTRYPOINT ["java", "-jar", "app.jar"]

# Yeni portu expose edin
EXPOSE 8081