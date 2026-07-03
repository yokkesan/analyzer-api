FROM eclipse-temurin:21-jdk

RUN apt-get update && \
    apt-get install -y git

WORKDIR /app

COPY . .

RUN chmod +x gradlew
RUN chmod +x start.sh

ENV GRADLE_OPTS="-Dorg.gradle.vfs.watch=true"

EXPOSE 8080

CMD ["./start.sh"]