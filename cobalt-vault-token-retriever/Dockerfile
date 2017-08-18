FROM openjdk:8-jre-alpine
VOLUME /tmp
ARG VERSION="1.0-SNAPSHOT"
ADD target/cobalt-vault-token-retriever-${VERSION}.jar app.jar
ENV JAVA_OPTS=""
EXPOSE 8443
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]