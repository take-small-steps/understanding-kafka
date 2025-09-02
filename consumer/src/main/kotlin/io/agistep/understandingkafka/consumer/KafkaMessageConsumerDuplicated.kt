package io.agistep.understandingkafka.consumer

import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Service

@Service
class KafkaMessageConsumerDuplicated {
    
    private val logger = LoggerFactory.getLogger(KafkaMessageConsumerDuplicated::class.java)
    
    @KafkaListener(topics = ["loopers-example-topic"], groupId = "loopers-group2")
    fun consumeMessage(
        @Payload message: String,
        @Header(KafkaHeaders.RECEIVED_KEY, required = false) key: String?,
        @Header(KafkaHeaders.RECEIVED_TOPIC) topic: String,
        @Header(KafkaHeaders.RECEIVED_PARTITION) partition: Int,
        @Header(KafkaHeaders.OFFSET) offset: Long,
        acknowledgment: Acknowledgment?
    ) {
        logger.info(
            "[KafkaMessageConsumerDuplicated] Received message from topic '{}', partition: {}, offset: {}, key: '{}', message: '{}'",
            topic,
            partition,
            offset,
            key ?: "null",
            message
        )
        
        try {
            // 메시지 처리 로직
            processMessage(message, key)
            

            acknowledgment?.acknowledge()
            
        } catch (e: Exception) {
            logger.error("[KafkaMessageConsumerDuplicated] Error processing message: {}", e.message, e)

        }
    }
    
    private fun processMessage(message: String, key: String?) {
        logger.info("[KafkaMessageConsumerDuplicated] Processing message with key '{}': {}", key ?: "null", message)

        // 시뮬레이션: 메시지 처리 시간
        Thread.sleep(100)
        
        logger.info("[KafkaMessageConsumerDuplicated] Message processed successfully")
    }
}
