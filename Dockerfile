FROM bellsoft/liberica-openjdk-alpine-musl
COPY ./target/inet-back-0.0.1-SNAPSHOT.jar .
CMD ["java","-jar","inet-back-0.0.1-SNAPSHOT.jar"]