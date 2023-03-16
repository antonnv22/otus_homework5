kubectl apply -f postgres-config.yaml
kubectl apply -f migration.yaml
kubectl apply -f postgres-config-backend.yaml
kubectl apply -f postgres-secret-backend.yaml
kubectl apply -f backend-services-config.yaml
kubectl apply -f profileservice-deployment.yaml
kubectl apply -f gateway-deployment.yaml