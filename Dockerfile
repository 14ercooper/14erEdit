FROM maven:3.8.3-eclipse-temurin-11 AS buildEditJar
RUN echo "nameserver 1.1.1.1" > /etc/resolv.conf
WORKDIR /14erEditCode
ADD . .
RUN mvn clean test package
WORKDIR /
RUN cp /14erEditCode/target/14erEdit-*.jar 14erEdit.jar

FROM ghcr.io/graalvm/graalvm-ce:java17-21
WORKDIR /14erEdit
COPY --from=buildEditJar /14erEdit.jar 14erEdit.jar
RUN mkdir mms
ENTRYPOINT cd /14erEdit/mms && java -jar ../14erEdit.jar

