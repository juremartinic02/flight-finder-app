# Step 1: Build the App
FROM eclipse-temurin:17-jdk-focal AS builder

WORKDIR /app

# If files don't change, Docker reuses previous work, speeding up builds
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Copy all actual Java source code (including application.properties and application-local.properties)
# This will ensure that the app runs properly with test api keys
COPY src ./src

# Tell Maven to build your project into a runnable JAR file.
# Skip tests here (-DskipTests) to make this Docker build faster.
RUN ./mvnw package -DskipTests

# Step 2: Create the Final Lean Image to Run the App
FROM eclipse-temurin:17-jre-focal

# Set up an '/app' folder.
WORKDIR /app

# Variable holding the path to built JAR file from Stage 1
ARG JAR_FILE=/app/target/backend-0.0.1-SNAPSHOT.jar

# Copy ONLY the runnable JAR file built in Step 1 into this new clean image
COPY --from=builder ${JAR_FILE} app.jar

# We are telling Docker that app inside this container will use port 8080
EXPOSE 8080

# This is the command that will run when the Docker container starts:
ENTRYPOINT ["java", "-jar", "/app/app.jar"]