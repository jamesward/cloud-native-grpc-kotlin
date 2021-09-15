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


## TODO

- `chat-server` configurable client connection info
- `chat-client` configurable client connection info
- `chat-client` GraalVM Native Image
- Kubernetes Deployment Descriptors
