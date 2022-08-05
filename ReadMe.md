# Project 80

Spring Boot & Kafka

https://gitorko.github.io/

```bash
cd docker
docker-compose up -d
docker exec -it kafkaserver /bin/bash
/opt/bitnami/kafka/bin/kafka-topics.sh --create --replication-factor 1 --partitions 1 --topic mytopic --bootstrap-server localhost:9092
```
