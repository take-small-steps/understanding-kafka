package io.agistep.understandingkafka.config

import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka

@Configuration
@EnableKafka
class KafkaConfig {
    // Kafka 설정은 application.properties에서 자동으로 적용됩니다.
    // 필요한 경우 여기에 추가적인 커스텀 설정을 할 수 있습니다.
}
