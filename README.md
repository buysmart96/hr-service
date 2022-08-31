![enter image description here](https://codeobe.cloud/_nuxt/img/logo.bd4fcba.png)
# **Codeobe Sample Integration Code:**

# How to run this project on IDE?
   - Set up a new Debug Configuration.
   - Set the following application properties.
> #General properties <br>
> api.package.name = <YOUR_PACKAGE_NAME > <br>
> server.port = <PORT_NUMBER> <br>
> output.http_endpoint=<THIRD_PARTY_API_ADDRESS> <br>
>
> #Logging properties.
> log.src_app=<LOGGING_SRC_APP_NAME> <br>
> log.target_app=<LOGGING_TARGET_APP_NAME> <br>
> log.process.group=<> <br>
> log.jms_destination=<JMS_DESTINATION_PATH> <br>

# How to run this as a standalone jar?
 - Copy the application.properties file from src/main/resources to project root directory and run build the project.
```
   $ cp src/main/resourcs/application.properties .
   $ mvn clean install
```
- Run the file using following command.

```aidl
   $ java -jar target/<jar_file_name>.jar
```
_NOTE: Make sure application.properties file is available on the same folder as the jar_

# How to run as a docker image?

- Make the dockerfile.
> #Dockerfie configurations <br>
> FROM openjdk:8-jre-alpine3.9 <br>
> ARG JAR_FILE=target/*.jar <br>
> COPY ${JAR_FILE} hr_service-1.0.0.jar <br>
> COPY application.properties /application.properties <br>
> ENTRYPOINT ["java","-jar","/hr_service-1.0.0.jar"] <br>

- Build the Dockerimage. NOTE: change directory path of the terminal to the project directory.
```aidl
   $ docker build -t <targt_name> .
```
 - Check whether the docker image has been built or not.
```aidl
   $ docker images
```

- Run the docker image as follows.
```aidl
   $ docker run -p 8099:8099 <image_name>
```

_NOTE:container port should be the one mentioned in the properties file._


# How to deploy to Codeobe platform?
added a new note to readme.
added for commiting.

