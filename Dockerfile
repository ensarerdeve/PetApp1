# Temel image olarak OpenJDK 17 kullanıyoruz
FROM openjdk:17-jdk-slim

# Uygulamanın çalışacağı dizini oluşturuyoruz
WORKDIR /app

# Maven'in bağımlılıkları yüklerken cache kullanılmasını sağlamak için bu dosyaları kopyalıyoruz
COPY pom.xml .
COPY src ./src

# Maven'i kullanarak uygulamayı derliyoruz
RUN apt-get update && apt-get install -y maven
RUN mvn clean package -DskipTests

# Uygulama jar dosyasını kopyalıyoruz
COPY target/*.jar app.jar

# Ortam değişkenlerini tanımla
ENV FIREBASE=/home/petdkqsa/photos
ENV STORAGE=/home/petdkqsa/photos

# Uygulamanın çalıştırılacağı portu belirliyoruz
EXPOSE 8080

# Uygulamayı başlatıyoruz
ENTRYPOINT ["java", "-jar", "app.jar"]
