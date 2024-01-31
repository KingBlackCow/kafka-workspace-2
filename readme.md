
```angular2html
docker run -it --rm \
 --name=kcat \
 -v "$(pwd)/file.txt:/app/file.txt" \
 --network=fastcampus-kafka-message-queue_default \
 edenhill/kcat:1.7.1 \
 -b kafka1:19092,kafka2:19092,kafka3:19092 \
 -P -t js-json-topic -l -K: /app/file.txt
```