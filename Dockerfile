FROM maven:3.8.3-eclipse-temurin-11 AS buildEditJar
RUN echo "nameserver 1.1.1.1" > /etc/resolv.conf
WORKDIR /14erEditCode
ADD . .
RUN mvn clean test package
WORKDIR /
RUN cp /14erEditCode/target/14erEdit-*.jar 14erEdit.jar

FROM ghcr.io/graalvm/graalvm-ce:java17-21
LABEL \
	org.opencontainers.image.url='https://github.com/users/14ercooper/packages/container/package/mapmaking_megaserver' \
	org.opencontainers.image.source='https://github.com/14ercooper/14erEdit' \
	org.opencontainers.image.title='The Mapmaking Megaserver' \
	org.opencontainers.image.authors='The 14erEdit Team' \
	org.opencontainers.image.description='The Mapmaking Megaserver is a tool to easily and quickly set up a local server for running common serverside Minecraft mapmaking tools.'
WORKDIR /14erEdit
COPY --from=buildEditJar /14erEdit.jar 14erEdit.jar
RUN mkdir mms
ENTRYPOINT cd /14erEdit/mms && java -jar ../14erEdit.jar

