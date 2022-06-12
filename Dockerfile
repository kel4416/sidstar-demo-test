FROM openjdk:18
EXPOSE 8080
ADD target/sidstar-test-docker.jar sidstar-test-docker.jar
ENTRYPOINT ["java","-jar","sidstar-test-docker.jar"]