package com.example.kafka.consumer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.support.Acknowledgment;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MessageConsumerServiceTest {

    private MessageConsumerService messageConsumerService;

    @Mock
    private Acknowledgment acknowledgment;

    @BeforeEach
    void setUp() {
        messageConsumerService = new MessageConsumerService();
    }

    @Test
    void listen_shouldAcknowledgeMessageOnSuccess() {
        String message = "test-message";
        messageConsumerService.listen(message, acknowledgment);
        // Verify that acknowledge was called
        verify(acknowledgment, times(1)).acknowledge();
        verify(acknowledgment, times(1)).acknowledge();
    }

    @Test
    void listen_shouldThrowRuntimeExceptionForErrorMessage() {
        String errorMessage = "error-message";
        assertThrows(RuntimeException.class, () -> messageConsumerService.listen(errorMessage, acknowledgment));
        // Verify that acknowledge was NOT called
        verify(acknowledgment, never()).acknowledge();
        verify(acknowledgment, never()).acknowledge(); // Should not acknowledge on error
    }
}