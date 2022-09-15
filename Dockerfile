FROM openjdk:8-jre-alpine3.9
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} hr_service-1.0.0.jar
COPY application.properties /application.properties
ENTRYPOINT ["java","-jar","/hr_service-1.0.0.jar"]

#DO NOT MODIFY FOLLOWING
ARG carg1=host
ARG carg2=user
ARG carg3=pass
ARG carg4=in
ARG carg5=out
ARG carg5=base 

ENV cenv1=${carg1}
ENV cenv2=${carg2}
ENV cenv3=${carg3}
ENV cenv4=${carg4}
ENV cenv5=${carg5}
ENV cenv6=${carg6}
#DO NOT MODIFY ABOVE



