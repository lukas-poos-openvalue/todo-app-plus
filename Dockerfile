FROM gcr.io/distroless/java25-debian13:nonroot

WORKDIR /app

ARG VERSION="0.0.1"
COPY target/todo-app-plus-${VERSION}.jar todo-app-plus.jar

EXPOSE 8080
ENTRYPOINT ["java", "--enable-native-access=ALL-UNNAMED", "-jar", "todo-app-plus.jar"]