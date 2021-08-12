package com.schooldevops.sqs.jms.controllers;

import com.schooldevops.sqs.jms.MessageSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class MessageController {

    @Autowired
    MessageSender messageSender;

    @PostMapping("/message/{msg}")
    public String sendMessage(@PathVariable("msg") String message) {
        messageSender.send(message);
        log.info("Sent Message : {}", message);
        return message;
    }
}
