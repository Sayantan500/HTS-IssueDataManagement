ARG image_tag_jdk=17-alpine3.14-jdk
ARG image_tag_jre=17-al2023-headless

# Stage 1 of build --> building the project and creating jar file
FROM amazoncorretto:$image_tag_jdk AS app_jar_builder
WORKDIR /app/
COPY src/ ./src/
COPY pom.xml ./
COPY .mvn/ ./'.mvn'/
COPY mvnw ./
RUN ["./mvnw","clean","install","spring-boot:repackage"]

#Stage 2 of build --> copying the jar file from previous stage and building the final image
FROM amazoncorretto:$image_tag_jre
WORKDIR /app/
COPY --from=app_jar_builder /app/target/HTS_Issues_management_System.jar ./
EXPOSE 8080
ENTRYPOINT ["java","-jar","HTS_Issues_management_System.jar"]