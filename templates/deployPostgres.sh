#!/usr/bin/env sh
kubectl create ns backend
kubectl create ns postgres

kubectl apply -f postgres-secret.yaml
helm -n postgres install otus -f my-postgresql-values.yaml ./postgresql