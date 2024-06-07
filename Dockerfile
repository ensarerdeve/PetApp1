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

# JSON dosyasını kopyala
COPY src/main/resources/chat-service1-firebase-adminsdk-hplj2-5cbc50ef5e.json /home/petdkqsa/photos/

# Ortam değişkenlerini tanımla
ENV firebase=/home/petdkqsa/photos/chat-service1-firebase-adminsdk-hplj2-5cbc50ef5e.json
ENV storage=/home/petdkqsa/photos

# Uygulamayı başlatıyoruz
ENTRYPOINT ["java", "-jar", "app.jar"]
