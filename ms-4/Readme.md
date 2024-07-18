mvn clean install
docker build -t ms-4 .
docker tag ms-4 localhost:5000/logs/ms-4
docker push localhost:5000/logs/ms-4