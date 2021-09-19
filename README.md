# Cloud Native gRPC Kotlin

Start the Explicit Filter server:
```
./gradlew :ef-server:run
```

Start the Chat server:
```
./gradlew :chat-server:run
```

Run the Chat client:
```
./gradlew :chat-client:run --console=plain --quiet
```

Create & run the Chat client native image:
```
./gradlew :chat-client:nativeImage
chat-client/build/graal/chat
```

Containerize & Run Locally:
```
./gradlew :ef-server:jibDockerBuild
docker run -it -p 50051:50051 ef-server

./gradlew :chat-server:jibDockerBuild
docker run -it -p 50052:50052 chat-server
```

Docker Cleanup

```
docker system prune -f
docker volume prune
```

Containerize (Remote Storage):
```
./gradlew :ef-server:jib --image=gcr.io/$PROJECT_ID/ef-server
./gradlew :chat-server:jib --image=gcr.io/$PROJECT_ID/chat-server
```

## TODO

- `chat-server` configurable client connection info (env var or arg)
- `chat-client` configurable client connection info (env var or arg)
- Kubernetes Deployment Descriptors
  - make sure we use http2
