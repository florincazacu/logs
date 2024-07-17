mvn clean install
docker build -t ms-1 .
docker tag ms-1 localhost:5000/logs/ms-1
docker push localhost:5000/logs/ms-1
kubectl apply -f ./k8s/deployment.yml
kubectl delete -f ./k8s/deployment.yml