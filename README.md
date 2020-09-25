
# Spring Boot - Zoo Application
Build Rest API for Zoo Application

•	Open the project into IntelliJ 
•	Run the ZooApplication.java can also run using command 
•	Reach till spring boot application folder (zoo-app)
•	Run this command (mvn spring-boot:run)

API Details :
http://localhost:8090/swagger-ui.html#


# Docker packaging
docker build --tag zoo-app .

docker run -p 8080:8090 -t zoo-app --name zoo-app

Access the application using URL : http://192.168.99.100:8080/api/room