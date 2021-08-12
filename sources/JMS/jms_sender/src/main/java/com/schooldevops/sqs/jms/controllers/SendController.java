package com.schooldevops.sqs.jms.controllers;

import com.schooldevops.sqs.jms.services.SQSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SendController {

    @Autowired
    SQSService sqsService;

    @PostMapping("/message/{msg}")
    public String sendMsg(@PathVariable("msg") String msg) {
        return sqsService.send("my-todo2", msg);
    }
}
