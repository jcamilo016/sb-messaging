package com.eafit.sac.services.messaging.service;

import com.eafit.sac.services.messaging.config.EmailConfig;
import com.eafit.sac.services.messaging.config.SMSConfig;
import com.eafit.sac.services.messaging.dto.SendSMSRequest;
import com.eafit.sac.services.messaging.entity.Message;
import com.eafit.sac.services.messaging.exception.RepositoryRequestException;
import com.eafit.sac.services.messaging.exception.SendEmailException;
import com.eafit.sac.services.messaging.exception.SendSMSException;
import com.eafit.sac.services.messaging.repository.MessageRepository;
import com.eafit.sac.services.messaging.utils.MessageStatus;
import com.twilio.Twilio;
import com.twilio.exception.ApiException;
import com.twilio.type.PhoneNumber;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    SMSConfig smsConfig;

    @Autowired
    EmailConfig emailConfig;

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private ModelMapper modelMapper;

    public Message sendSMSMessage(SendSMSRequest smsRequest) {
        Message message = modelMapper.map(smsRequest, Message.class);

        try {
            Twilio.init(smsConfig.getAccountSID(), smsConfig.getAuthToken());
            com.twilio.rest.api.v2010.account.Message smsClient = com.twilio.rest.api.v2010.account.Message.creator(
                    new PhoneNumber("+57" + smsRequest.getSendTo()),
                    smsConfig.getMessageSID(),
                    smsRequest.getMessage()
            ).create();

            System.out.println(smsClient.getSid() + " : " + smsClient.getStatus());

            message.setSentStatus(MessageStatus.SENT);
        } catch (ApiException ex) {
            message.setSentStatus(MessageStatus.NOT_SENT);
            logMessageSendAction(message);
            throw new SendSMSException(ex.getMessage());
        }

        return logMessageSendAction(message);
    }

    public List<Message> listAllRecords() {
        return Optional.of(messageRepository.findByOrderBySentDateDesc())
                .orElseThrow(() -> new RepositoryRequestException("An error occurred while obtaining the info from database"));
    }

    public List<Message> listByReceiver(String receiver) {
        return Optional.of(messageRepository.findBySendTo(receiver))
                .orElseThrow(() -> new RepositoryRequestException("An error occurred while obtaining the info from database"));
    }

    public Message logMessageSendAction(Message sentMessage) {
        return Optional.of(messageRepository.save(sentMessage))
                .orElseThrow(() -> new RepositoryRequestException("An error occurred while saving the information in the database"));
    }

    public Message sendEmailMessage(String to, String subject, String text) {
        Message sentMessage = new Message();
        sentMessage.setSendTo(to);
        sentMessage.setMessage(subject);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(emailConfig.getFrom());
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        try {
            emailSender.send(message);
            sentMessage.setSentStatus(MessageStatus.SENT);
        } catch (MailException ex) {
            sentMessage.setSentStatus(MessageStatus.NOT_SENT);
            logMessageSendAction(sentMessage);
            throw new SendEmailException(ex.getMessage());
        }

        return logMessageSendAction(sentMessage);
    }
}
