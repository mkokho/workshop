# Image with java from the wild Internet.
# Use image from Digital Shared Platforms team for produciton.
FROM anapsix/alpine-java:8_server-jre

RUN mkdir /app
WORKDIR /app

COPY app.jar /app/
COPY etc     /app/etc

EXPOSE 8080 8081

# Always run app.jar.
ENTRYPOINT ["java", "-jar", "app.jar"]

# Show help and exit. See readme on how to start the server
CMD ["-h"]
