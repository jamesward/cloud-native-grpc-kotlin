# Cloud Native gRPC Kotlin

## Run with just test containers

The following command builds both the server and the explicit filter containers and launches them as test containers.

Run Chat Client against Server Testcontainers:
```
./gradlew :chat-client:runc --console=plain --quiet
```


## Run everything manually 

Start the Explicit Filter server:
```
./gradlew :ef-server:run
```

Start the Chat server:
```
./gradlew :chat-server:run
```

Start the Chat web app:
```
./gradlew :chat-web:run
```
[web app](http://localhost:8080)

Run the Chat client:
```
./gradlew :chat-client:run --console=plain --quiet
```

Create & run the Chat client native image:
```
./gradlew :chat-client:nativeImage
chat-client/build/graal/chat
```

## Build and Run the containers manually

Containerize & Run Locally:
```
./gradlew :ef-server:jibDockerBuild
docker run -it --net=host ef-server

./gradlew :chat-server:jibDockerBuild
docker run -it --net=host chat-server

./gradlew :chat-web:jibDockerBuild
docker run -it --net=host chat-web
```

## Cloudy version -  Run everything in GCP

In GCP console

```
export PROJECT_ID=project id in the browser address bar
```

Find GKE connect string in GUI (screen shot)

```
gcloud container clusters get-credentials cngk --region us-central1 --project none-219021
```

Containerize (Remote Storage):
```
./gradlew :ef-server:jib --image=gcr.io/$PROJECT_ID/ef-server
./gradlew :chat-server:jib --image=gcr.io/$PROJECT_ID/chat-server
./gradlew :chat-web:jib --image=gcr.io/$PROJECT_ID/chat-web
```

Run in GKE:

TODO: chat-web
```
cd kubernetes/
kubectl create -f ef-server.yml
kubectl create -f ef-server-service.yml
kubectl create -f chat-server.yml
kubectl create -f chat-server-service.yml
kubectl get svc
kubectl get pods
kubectl logs -f chat-server-7d96b44fdd-pd28f
```

Back on in your local environment:

```
export CHAT_SERVER_SERVER_TARGET=34.66.212.0:50052
./gradlew :chat-client:run --console=plain --quiet
```


Clean-up

```
kubectl delete -f ef-server.yml
kubectl delete -f ef-server-service.yml
kubectl delete -f chat-server.yml
kubectl delete -f chat-server-service.yml
```

## TODO

- `chat-server` configurable client connection info (env var or arg)
- `chat-client` configurable client connection info (env var or arg)
- Kubernetes Deployment Descriptors
  - make sure we use http2
