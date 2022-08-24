FROM openjdk:8-jre-alpine3.9
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} hr_service-1.0.0.jar
COPY application.properties /application.properties
ENTRYPOINT ["java","-jar","/hr_service-1.0.0.jar"]

#DO NOT MODIFY FOLLOWING
ARG carg1=htpp://localhost:8080
ARG carg2=htpp://localhost:8080
ARG carg3=codeobe
ARG carg4=x
ARG carg5=x
ENV cenv1=${carg1}
ENV cenv2=${carg2}
ENV cenv3=${carg3}
ENV cenv4=${carg4}
ENV cenv5=${carg5}
#DO NOT MODIFY ABOVE



