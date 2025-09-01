package io.agistep.understandingkafka.consumer

import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Service

@Service
class KafkaMessageConsumer {
    
    private val logger = LoggerFactory.getLogger(KafkaMessageConsumer::class.java)
    
    @KafkaListener(topics = ["\${kafka.topic.name}"], groupId = "\${spring.kafka.consumer.group-id}")
    fun consumeMessage(
        @Payload message: String,
        @Header(KafkaHeaders.RECEIVED_KEY, required = false) key: String?,
        @Header(KafkaHeaders.RECEIVED_TOPIC) topic: String,
        @Header(KafkaHeaders.RECEIVED_PARTITION) partition: Int,
        @Header(KafkaHeaders.OFFSET) offset: Long,
        acknowledgment: Acknowledgment?
    ) {
        logger.info(
            "[CONSUMER] Received message from topic '{}', partition: {}, offset: {}, key: '{}', message: '{}'",
            topic,
            partition,
            offset,
            key ?: "null",
            message
        )
        
        try {
            // 메시지 처리 로직
            processMessage(message, key)
            
            // 메시지 처리 완료 후 수동으로 커밋 (필요한 경우)
            acknowledgment?.acknowledge()
            
        } catch (e: Exception) {
            logger.error("[CONSUMER] Error processing message: {}", e.message, e)
            // 에러 처리 로직을 여기에 구현할 수 있습니다
        }
    }
    
    private fun processMessage(message: String, key: String?) {
        logger.info("[CONSUMER] Processing message with key '{}': {}", key ?: "null", message)
        
        // 실제 비즈니스 로직을 여기에 구현합니다
        // 예: 데이터베이스 저장, 외부 API 호출, 파일 처리 등
        
        // 시뮬레이션: 메시지 처리 시간
        Thread.sleep(100)
        
        logger.info("[CONSUMER] Message processed successfully")
    }
}
