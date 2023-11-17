FROM amazoncorretto:11
COPY build/libs/hotelier-webservices-0.0.1-SNAPSHOT.jar hotelier-webservices.jar
EXPOSE 8080
CMD ["java", "-jar", "-Dsvnkit.http.methods=NTLM", "./hotelier-webservices.jar"]