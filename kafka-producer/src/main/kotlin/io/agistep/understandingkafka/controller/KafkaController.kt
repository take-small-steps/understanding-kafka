package io.agistep.understandingkafka.controller

import io.agistep.understandingkafka.producer.KafkaProducer
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/kafka")
class KafkaController(
    private val kafkaProducer: KafkaProducer
) {
    
    @PostMapping("/send")
    fun sendMessage(@RequestBody request: MessageRequest): ResponseEntity<MessageResponse> {
        return try {
            for (i in 1..50) {
                kafkaProducer.sendMessage(request.message)
            }
            ResponseEntity.ok(
                MessageResponse(
                    success = true,
                    message = "Message sent successfully: ${request.message}"
                )
            )
        } catch (e: Exception) {
            ResponseEntity.internalServerError().body(
                MessageResponse(
                    success = false,
                    message = "Failed to send message: ${e.message}"
                )
            )
        }
    }
    
    @PostMapping("/send-with-key")
    fun sendMessageWithKey(@RequestBody request: MessageWithKeyRequest): ResponseEntity<MessageResponse> {
        return try {
            for (i in 1..50) {
                kafkaProducer.sendMessage(request.key, request.message)
            }

            ResponseEntity.ok(
                MessageResponse(
                    success = true,
                    message = "Message sent successfully with key '${request.key}': ${request.message}"
                )
            )
        } catch (e: Exception) {
            ResponseEntity.internalServerError().body(
                MessageResponse(
                    success = false,
                    message = "Failed to send message: ${e.message}"
                )
            )
        }
    }
    
    @GetMapping("/send-test")
    fun sendTestMessage(): ResponseEntity<MessageResponse> {
        return try {
            val testMessage = "Test message at ${java.time.LocalDateTime.now()}"
            kafkaProducer.sendMessage(testMessage)
            ResponseEntity.ok(
                MessageResponse(
                    success = true,
                    message = "Test message sent successfully: $testMessage"
                )
            )
        } catch (e: Exception) {
            ResponseEntity.internalServerError().body(
                MessageResponse(
                    success = false,
                    message = "Failed to send test message: ${e.message}"
                )
            )
        }
    }
}

data class MessageRequest(
    val message: String
)

data class MessageWithKeyRequest(
    val key: String,
    val message: String
)

data class MessageResponse(
    val success: Boolean,
    val message: String
)
