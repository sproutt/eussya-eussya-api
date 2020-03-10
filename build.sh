docker rm `docker ps -a -q`
docker rmi `docker images -q`

docker build --build-arg JAR_FILE=build/libs/*.jar -t sproutt/eussya-eussya-api .