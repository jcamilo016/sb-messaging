package com.eafit.sac.services.messaging.controller;

import com.eafit.sac.services.messaging.dto.SendEmailRequest;
import com.eafit.sac.services.messaging.entity.Message;
import com.eafit.sac.services.messaging.service.MessageService;
import com.eafit.sac.services.messaging.dto.SendSMSRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Operation(summary = "Get the list of messaging log activity, it can be filtered by the receiver")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the messages log activity")})
    @GetMapping(value = "/")
    public ResponseEntity<List<Message>> listLogRegistries(@RequestParam(required = false) String receiver) {
        List<Message> logRegistries;
        if (StringUtils.isBlank(receiver)) {
            logRegistries = messageService.listAllRecords();
        } else {
            logRegistries = messageService.listByReceiver(receiver);
        }

        return new ResponseEntity<>(logRegistries, HttpStatus.OK);
    }

    @Operation(summary = "Send an SMS message to the phone number and with the message specified in the body")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "SMS message sent"),
            @ApiResponse(responseCode = "406", description = "Invalid phone number",
                    content = @Content)
    })
    @PostMapping("/sendSms")
    public ResponseEntity<Message> sendSMSMessage(@RequestBody SendSMSRequest payload){
        Message messageSent = messageService.sendSMSMessage(payload);
        return new ResponseEntity<>(messageSent, HttpStatus.ACCEPTED);
    }

    @Operation(summary = "Send an email message to the email address with the subject and message body specified in the body")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Email message sent"),
            @ApiResponse(responseCode = "406", description = "Invalid email address provided",
                    content = @Content)
    })
    @PostMapping(value = "/sendMail")
    public ResponseEntity<Message> sendEmailMessage(@RequestBody SendEmailRequest sendEmailRequest) {
        Message messageSent = messageService
                .sendEmailMessage(sendEmailRequest.getTo(), sendEmailRequest.getSubject(), sendEmailRequest.getText());
        return new ResponseEntity<>(messageSent, HttpStatus.ACCEPTED);
    }
}
