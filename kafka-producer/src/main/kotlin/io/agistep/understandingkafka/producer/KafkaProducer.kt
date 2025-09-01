package io.agistep.understandingkafka.producer

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.SendResult
import org.springframework.stereotype.Service
import java.util.concurrent.CompletableFuture

@Service
class KafkaProducer(
    private val kafkaTemplate: KafkaTemplate<String, String>
) {
    
    private val logger = LoggerFactory.getLogger(KafkaProducer::class.java)
    
    @Value("\${kafka.topic.name}")
    private lateinit var topicName: String
    
    fun sendMessage(message: String): CompletableFuture<SendResult<String, String>> {
        logger.info("Sending message to topic '{}': {}", topicName, message)
        
        val future = kafkaTemplate.send(topicName, message)
        
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
        
        val future = kafkaTemplate.send(topicName, key, message)
        
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
