## Dropwizard in Docker container

Very simple application that will help you to make yours. 

Questions? Come and see us on the weekly Docker Session! 

### Usage
 - check the build by `./gradlew build`
 - start the server by `./gradlew run`
 - check the admin page on [http://localhost:8081/]()
 - check that app page on [http://localhost:8080/]()

### Docker Image
 - run `./gradlew shadowJar`
 - check that `app.jar` is created in ./build/libs directory
 - run `./gradlew preDockerBuild` which will copy all required files into ./docker folder
 - check that `app.jar` and `etc/*` are created in ./docker folder 
 - run `docker build --tag=sample ./docker` which will create image
 - run `docker run --rm -ti sample server etc/local.yml` which will start a container with config from local.yml file
  