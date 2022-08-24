1) how to run this in IDE ( debug configuration / app.properties / )
2) How to run as a standalone jar ( java -jar <jar_file_name>)
   app.properties file put into same file location.
3) How to run as a docker image ( )
   dockerfile
   docker build -t employee-registering-service-docker .
   docker images
   docker run -p 8099:8099 <imageId>
4) How to deploy to codeobe platform ( )


Build : Copy the application.properties file from src/main/resources to project root directory and issue maven install command
cp src/main/resourcs/application.properties .
mvn clean install

Start : java -jar target\spring-ms-template-1.0.0.jar

Notes : Make sure application.properties file is available on the same folder as the jar

Docuementation : http://localhost:8099/swagger-ui.html, http://localhost:8099/v2/api-docs

To check other instances via eureka : http://localhost:8081/service-info

Deploy to 10.124.4.50:8081

/*****************************/
changed the ip address of localhost to your com's ip. ( in activemq broker url)

docker build -t employee-registering-service-docker .
docker images
docker run -p 8099:8099 <imageId>

