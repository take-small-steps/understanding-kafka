package io.agistep.understandingkafka.producer

import org.apache.kafka.common.Uuid
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.SendResult
import org.springframework.stereotype.Service
import java.util.UUID
import java.util.concurrent.CompletableFuture

@Service
class KafkaProducer(
    private val kafkaTemplate: KafkaTemplate<String, String>
) {
    
    private val logger = LoggerFactory.getLogger(KafkaProducer::class.java)
    
    @Value("loopers-example-topic")
    private lateinit var topicName: String
    
    fun sendMessage(message: String): CompletableFuture<SendResult<String, String>> {
        logger.info("Sending message to topic '{}': {}", topicName, message + UUID.randomUUID().toString())
        
        val future = kafkaTemplate.send(topicName, message + UUID.randomUUID().toString())
        
        future.whenComplete { result, throwable ->
            if (throwable == null) {
                logger.info(
                    "Message sent successfully to topic '{}' with offset: {} and partition: {}",
                    topicName,
                    result.recordMetadata.offset(),
                    result.recordMetadata.partition()
                )
            } else {
                logger.error("Failed to send message to topic '{}': {}", topicName, throwable.message)
            }
        }
        
        return future
    }
    
    fun sendMessage(key: String, message: String): CompletableFuture<SendResult<String, String>> {
        logger.info("Sending message with key '{}' to topic '{}': {}", key, topicName, message)
        
//        val future = kafkaTemplate.send(topicName, key, """
//            "version": 0.0.2
//            "foo": "bar"
//        """.trimIndent())
        val future = kafkaTemplate.send(topicName, key, """
            "foo": "bar",
            "foov2": "bar2"
        """.trimIndent())
        
        future.whenComplete { result, throwable ->
            if (throwable == null) {
                logger.info(
                    "Message with key '{}' sent successfully to topic '{}' with offset: {} and partition: {}",
                    key,
                    topicName,
                    result.recordMetadata.offset(),
                    result.recordMetadata.partition()
                )
            } else {
                logger.error("Failed to send message with key '{}' to topic '{}': {}", key, topicName, throwable.message)
            }
        }
        
        return future
    }
}
