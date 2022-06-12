FROM openjdk:18
EXPOSE 8080
ADD /home/runner/.m2/repository/com/sidstar-test/sidstar-demo-test/0.0.1-SNAPSHOT/sidstar-demo-test-0.0.1-SNAPSHOT.jar sidstar-test-docker.jar
ENTRYPOINT ["java","-jar","sidstar-test-docker.jar"]
