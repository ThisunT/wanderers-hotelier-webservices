# FROM gradle:7.6.0-jdk-alpine AS build
# WORKDIR /app
# COPY ./ /app/
# RUN gradle clean build
#
# FROM amazoncorretto:11
# WORKDIR /app
# COPY --from=build /app/build/libs/hotelier-webservices-0.0.1-SNAPSHOT.jar /app
# EXPOSE 8080
# CMD ["java", "-jar", "-Dsvnkit.http.methods=NTLM", "./hotelier-webservices-0.0.1-SNAPSHOT.jar"]

# In case of a docker gradle build failure use the following instead of the above

FROM amazoncorretto:11
COPY build/libs/hotelier-webservices-0.0.1-SNAPSHOT.jar hotelier-webservices.jar
EXPOSE 8080
CMD ["java", "-jar", "-Dsvnkit.http.methods=NTLM", "./hotelier-webservices.jar"]