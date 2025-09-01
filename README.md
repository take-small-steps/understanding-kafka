# Understanding Kafka

이 프로젝트는 Apache Kafka의 Producer와 Consumer 기능을 테스트하기 위한 멀티모듈 Spring Boot 애플리케이션입니다.

## 프로젝트 구조

```
understanding-kafka/
├── build.gradle.kts            # 루트 빌드 설정
├── settings.gradle.kts         # 멀티모듈 설정
├── docker-compose.yml          # Kafka 실행 환경
├── main/                       # Producer 모듈
│   ├── build.gradle.kts
│   └── src/main/kotlin/io/agistep/understandingkafka/
│       ├── config/
│       │   └── KafkaConfig.kt          # Kafka 설정
│       ├── controller/
│       │   └── KafkaController.kt      # REST API 컨트롤러
│       ├── producer/
│       │   └── KafkaProducer.kt        # 메시지 전송 모듈
│       └── UnderstandingKafkaApplication.kt
└── consumer/                   # Consumer 모듈
    ├── build.gradle.kts
    └── src/main/kotlin/io/agistep/understandingkafka/
        ├── consumer/
        │   └── KafkaMessageConsumer.kt  # 메시지 수신 모듈
        └── ConsumerApplication.kt       # Consumer 애플리케이션
```

## 사전 준비사항

### 1. Kafka 설치 및 실행

```bash
# Docker Compose를 사용한 Kafka 실행 (추천)
docker-compose up -d

# 또는 로컬에 Kafka 설치 후 실행
# Zookeeper 실행
bin/zookeeper-server-start.sh config/zookeeper.properties

# Kafka 실행
bin/kafka-server-start.sh config/server.properties
```

### 2. 토픽 생성

```bash
# test-topic 토픽 생성
kafka-topics.sh --create --topic test-topic --bootstrap-server localhost:9092 --partitions 3 --replication-factor 1
```

## 애플리케이션 실행

### 1. Producer 모듈 실행 (메시지 전송용)

```bash
./gradlew :main:bootRun
```

### 2. Consumer 모듈 실행 (메시지 수신용) - 별도 터미널

```bash
./gradlew :consumer:bootRun
```

### 3. API 테스트 (Producer 모듈)

#### 간단한 테스트 메시지 전송 (GET 요청)
```bash
curl http://localhost:8080/api/kafka/send-test
```

#### 메시지 전송 (POST 요청)
```bash
curl -X POST http://localhost:8080/api/kafka/send \
  -H "Content-Type: application/json" \
  -d '{"message": "Hello Kafka from Producer!"}'
```

#### 키가 있는 메시지 전송
```bash
curl -X POST http://localhost:8080/api/kafka/send-with-key \
  -H "Content-Type: application/json" \
  -d '{"key": "user-123", "message": "User login event"}'
```

## 로그 확인

각 모듈을 실행하면 다음과 같은 로그를 확인할 수 있습니다:

### Producer 모듈 로그 (main 모듈)
```
INFO : Sending message to topic 'test-topic': Hello Kafka from Producer!
INFO : Message sent successfully to topic 'test-topic' with offset: 0 and partition: 1
```

### Consumer 모듈 로그 (consumer 모듈)
```
INFO : [CONSUMER] Received message from topic 'test-topic', partition: 1, offset: 0, key: 'null', message: 'Hello Kafka from Producer!'
INFO : [CONSUMER] Processing message with key 'null': Hello Kafka from Producer!
INFO : [CONSUMER] Message processed successfully
```

## 설정

### 모듈별 설정

#### main/src/main/resources/application.properties (Producer)
- `spring.application.name`: kafka-producer
- `kafka.topic.name`: 전송할 토픽 이름 (기본: test-topic)
- `spring.kafka.bootstrap-servers`: Kafka 서버 주소 (기본: localhost:9092)

#### consumer/src/main/resources/application.properties (Consumer)
- `spring.application.name`: kafka-consumer
- `kafka.topic.name`: 수신할 토픽 이름 (기본: test-topic)
- `spring.kafka.consumer.group-id`: Consumer 그룹 ID (기본: consumer-group)

### 주요 기능

1. **멀티모듈 구조**: Producer와 Consumer가 완전히 분리되어 독립 실행
2. **Producer 모듈** (`main`): 메시지 전송 및 REST API 제공
3. **Consumer 모듈** (`consumer`): 메시지 수신 및 처리
4. **독립 실행**: 각 모듈을 별도 프로세스로 실행 가능
5. **확장성**: 각 모듈을 독립적으로 스케일링 가능

## 테스트 시나리오

### 기본 테스트
1. **Kafka 실행**: `docker-compose up -d`
2. **Consumer 모듈 실행**: `./gradlew :consumer:bootRun` (백그라운드 실행)
3. **Producer 모듈 실행**: `./gradlew :main:bootRun`
4. **메시지 전송 테스트**: `curl http://localhost:8080/api/kafka/send-test`
5. **Consumer 로그 확인**: Consumer 모듈 콘솔에서 메시지 수신 확인

### 고급 테스트
1. **다중 인스턴스**: Consumer 모듈을 여러 개 실행하여 로드 밸런싱 테스트
2. **키 기반 메시지**: 파티션 분산 확인
3. **에러 처리**: Consumer 모듈을 종료했다가 재시작하여 메시지 복구 확인

### 모듈 특징
- **main 모듈**: Producer 기능과 REST API 제공 (포트: 8080)
- **consumer 모듈**: Consumer 기능만 제공, 별도 포트 없음 (메시지 처리만)
