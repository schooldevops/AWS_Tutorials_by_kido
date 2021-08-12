package com.schooldevops.sqs.jms;

import io.awspring.cloud.messaging.core.QueueMessagingTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MessageSenderWithTemplate {

    private static final String TEST_QUEUE = "my-todo2";

    @Autowired
    private QueueMessagingTemplate messagingTemplate;

    public void send(final String messagePayload) {

        Message<String> msg = MessageBuilder.withPayload(messagePayload)
                .setHeader("sender", "app1")
                .build();
        messagingTemplate.convertAndSend(TEST_QUEUE, msg);
        log.info("message sent");
    }

    public void sendToFifoQueue(final String messagePayload, final String messageGroupID, final String messageDedupID) {

        Message<String> msg = MessageBuilder.withPayload(messagePayload)
                .setHeader("message-group-id", messageGroupID)
                .setHeader("message-deduplication-id", messageDedupID)
                .build();
        messagingTemplate.convertAndSend(TEST_QUEUE, msg);
        log.info("message sent");
    }
}
