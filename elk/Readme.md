docker run -d -p 5000:5000 --restart=always --name registry registry:2
docker build -t elk .
docker tag elk localhost:5000/logs/elk
docker push localhost:5000/logs/elk