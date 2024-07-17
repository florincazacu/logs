mvn clean install
docker build -t ms-2 .
docker tag ms-2 localhost:5000/logs/ms-2
docker push localhost:5000/logs/ms-2