.PHONY: build

build:
	docker build  -f Dockerfile . -t antonnv22/gatewayservice:0.0.2

push:
	docker push antonnv22/gatewayservice:0.0.2

docker-start:
	docker-compose up -d

k8s-deploy:
	kubectl create ns backend
	cd .. && helm upgrade --install -n crud crud .\crud-chart

newman:
	cd .. && newman run crud-chart/postman/collection.json