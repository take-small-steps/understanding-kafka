package io.agistep.understandingkafka.config

import org.apache.kafka.clients.admin.NewTopic
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.config.TopicBuilder

@Configuration
@EnableKafka
class KafkaConfig {

    @Bean
    fun exampleTopic(): NewTopic {
        return TopicBuilder.name("loopers-example-topic")
            .partitions(2)
            .replicas(1)
            .build()
    }
}
