package com.eafit.sac.services.messaging.service;

import com.eafit.sac.services.messaging.config.EmailConfig;
import com.eafit.sac.services.messaging.config.SMSConfig;
import com.eafit.sac.services.messaging.dto.SendSMSRequest;
import com.eafit.sac.services.messaging.entity.Message;
import com.eafit.sac.services.messaging.exception.SendSMSException;
import com.eafit.sac.services.messaging.repository.MessageRepository;
import com.eafit.sac.services.messaging.utils.MessageStatus;
import com.twilio.rest.api.v2010.account.MessageCreator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MessageServiceTest {
    @Mock
    SMSConfig smsConfig;

    @Mock
    ModelMapper modelMapper;

    @Mock
    MessageRepository messageRepository;

    @Mock
    MessageCreator msgCreator;

    @Mock
    JavaMailSender mailSender;

    @Mock
    EmailConfig emailConfig;

    @Mock
    Message msg;

    @Mock
    com.twilio.rest.api.v2010.account.Message createdMessage;


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

        when(messageService.listAllRecords()).thenReturn(messages);

        List<Message> messagesResponse = messageService.listAllRecords();
        assertEquals(3, messagesResponse.size());
        verify(messageRepository, times(1)).findByOrderBySentDateDesc();
    }

    @DisplayName("Test list sending log records filtered by receiver")
    @Test
    void listByReceiver() {
        when(messageRepository.findBySendTo("3168684548")).thenReturn(Arrays.asList(
                new Message(1L, new Date(), "3168684548", MessageStatus.SENT, "mensaje prueba"),
                new Message(2L, new Date(), "3168684548", MessageStatus.NOT_SENT, "mensaje de prueba 2")
        ));

        List<Message> messagesTest = messageService.listByReceiver("3168684548");

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

    @DisplayName("Test if sms message sending fails")
    @Test
    void testErrorSendSMSMessage() {
        SendSMSRequest smsRequest = new SendSMSRequest("3168684548", "Test message");
        when(modelMapper.map(smsRequest, Message.class)).thenReturn(new Message(1L, new Date(), "3168684548", MessageStatus.SENT, "mensaje prueba"));
        when(smsConfig.getAccountSID()).thenReturn("AccountSID");
        when(smsConfig.getAuthToken()).thenReturn("authToken");
        when(smsConfig.getMessageSID()).thenReturn("messageSID");
        when(messageRepository.save(any(Message.class))).thenReturn(new Message(1L, new Date(), "3168684548", MessageStatus.SENT, "mensaje prueba"));

        assertThrows(SendSMSException.class, () -> messageService.sendSMSMessage(smsRequest));
    }

    @DisplayName("Test if email message is sent")
    @Test
    void testEmailMessageAction() {
        when(emailConfig.getFrom()).thenReturn("jcamilo016@hotmail.com");
        doNothing().when(mailSender).send(any(SimpleMailMessage.class));
        when(messageRepository.save(any(Message.class))).thenReturn(new Message(1L, new Date(), "jcamilo016@gmail.com", MessageStatus.SENT, "mensaje prueba"));

        Message sentSMS = messageService.sendEmailMessage("jcamilo016@gmail.com", "Asunto prueba", "Cuerpo prueba");

        assertEquals("jcamilo016@gmail.com", sentSMS.getSendTo());
        assertEquals(MessageStatus.SENT, sentSMS.getSentStatus());
    }
}