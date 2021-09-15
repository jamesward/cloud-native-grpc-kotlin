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
docker run -it --network=host ef-server

./gradlew :chat-server:jibDockerBuild
docker run -it --network=host chat-server
```


Containerize (Remote Storage):
```
./gradlew :ef-server:jib --image=gcr.io/$PROJECT_ID/ef-server
./gradlew :chat-server:jib --image=gcr.io/$PROJECT_ID/chat-server
```

## TODO

- `chat-server` configurable client connection info
- `chat-client` configurable client connection info
- Kubernetes Deployment Descriptors
