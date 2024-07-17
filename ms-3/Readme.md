mvn clean install
docker build -t ms-3 .
docker tag ms-3 localhost:5000/logs/ms-3
docker push localhost:5000/logs/ms-3