package com.eafit.sac.services.messaging.service;

import com.eafit.sac.services.messaging.entity.Message;
import com.eafit.sac.services.messaging.repository.MessageRepository;
import com.eafit.sac.services.messaging.utils.MessageStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
class MessageServiceTest {

    @Mock
    MessageRepository messageRepository;

    @Mock
    Message msg;

    @InjectMocks
    MessageService messageService;

    @DisplayName("Test list all sending log records")
    @Test
    void listAllRecords() {
        List<Message> messages = Arrays.asList(
                new Message(1L, new Date(), "jcamilo016@gmail.com", MessageStatus.SENT, "mensaje prueba"),
                new Message(2L, new Date(), "juanramiro@gmail.com", MessageStatus.NOT_SENT, "mensaje de prueba 2"),
                new Message(3L, new Date(), "katherinfr@gmail.com", MessageStatus.SENT, "mensaje de prueba 3")
        );

        when(messageRepository.findByOrderBySentDateDesc()).thenReturn(messages);
        assertEquals(3, messageRepository.findByOrderBySentDateDesc().size());
        verify(messageRepository, times(1)).findByOrderBySentDateDesc();
    }

    @DisplayName("Test list sending log records filtered by receiver")
    @Test
    void listByReceiver() {
        when(messageRepository.findBySendTo("3168684548")).thenReturn(Arrays.asList(
                new Message(1L, new Date(), "3168684548", MessageStatus.SENT, "mensaje prueba"),
                new Message(2L, new Date(), "3168684548", MessageStatus.NOT_SENT, "mensaje de prueba 2")
        ));

        List<Message> messagesTest = messageRepository.findBySendTo("3168684548");

        assertEquals(2, messagesTest.size());
        assertEquals("mensaje prueba", messagesTest.get(0).getMessage());
        verify(messageRepository, times(1)).findBySendTo("3168684548");
    }

    @DisplayName("Test if log records is saved")
    @Test
    void logMessageSendAction() {
        when(messageRepository.save(msg)).thenReturn(new Message(1L, new Date(), "3168684548", MessageStatus.SENT, "mensaje prueba"));

        Message testedMsg = messageService.logMessageSendAction(msg);

        assertEquals("mensaje prueba", testedMsg.getMessage());
        verify(messageRepository, times(1)).save(msg);
    }
}