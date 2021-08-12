package com.schooldevops.sqs.jms;


import io.awspring.cloud.messaging.listener.SqsMessageDeletionPolicy;
import io.awspring.cloud.messaging.listener.annotation.SqsListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MessageReceiver {
    @SqsListener(value = "my-todo2", deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS )
    public void receiveStringMessage(final String message,
                                     @Header("SenderId") String senderId) {
        log.info("message received {} {}",senderId,message);
    }
}
