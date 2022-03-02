# CodeCharacter Server 2022

### Code Generation Process

From project root run

```sh
$ openapi-generator-cli generate \
    -g kotlin-spring \
    -i docs/spec/CodeCharacter-API.yml \
    -c docs/spec/generator-config.yml \
    --model-name-suffix=Dto \
    --import-mappings=DateTime=java.time.Instant \
    --type-mappings=DateTime=java.time.Instant \
    -o library \
    --enable-post-process-file
```

### API Docs:

- [Specification](spec/index.html)

### Project Test/Coverage details:

- [Gradle Test Summary](test/index.html)
- [JaCoCo Coverage Summary](coverage/index.html)
