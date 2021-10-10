FROM adoptopenjdk/openjdk11
EXPOSE 5500
COPY target/MoneyC2C-0.0.1-SNAPSHOT.jar moneyC2C.jar
ENTRYPOINT ["java", "-jar", "/moneyC2C.jar"]