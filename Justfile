# Default Just Commands 
default:
    just --list

# Install Dependencies with Maven
install:
    mvn clean install

# Install Dependencies with Maven (when having Build Issues)
install-clean:
    mvn clean install -U

# Run with Maven
run:
    mvn spring-boot:run

# Run with Java
run-java:
    mvn clean package
    java -jar target/todo-app-1.0.0.jar

# Build JAR
build-jar:
    mvn clean package -DskipTests

# Run with production profile
run-java-prod:
    java -jar target/todo-app-1.0.0.jar --spring.profiles.active=prod

# Run tests
test:
    mvn test

# Build docker image
docker-build:
    docker build -t ranckosolutionsinc/spring-todo-app:latest .

# Run docker container
docker-run:
    docker run -p 8484:8484 ranckosolutionsinc/spring-todo-app:latest

# Run docker container with production profile
docker-run-prod:        
    docker run -p 8484:8484 ranckosolutionsinc/spring-todo-app:latest --spring.profiles.active=prod

# Compose up
compose-up:
    docker-compose up -d

# Compose down
compose-down:
    docker-compose down

# Compose build
compose-build:  
    docker-compose build

# Compose logs
compose-logs:
    docker-compose logs -f

# Compose down and up
compose-restart:
    docker-compose down
    docker-compose up -d

                
