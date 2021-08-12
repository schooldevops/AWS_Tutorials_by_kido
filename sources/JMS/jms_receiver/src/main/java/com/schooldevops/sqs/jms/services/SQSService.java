package com.schooldevops.sqs.jms.services;

import com.amazon.sqs.javamessaging.AmazonSQSMessagingClientWrapper;
import com.amazon.sqs.javamessaging.SQSConnection;
import com.amazon.sqs.javamessaging.SQSConnectionFactory;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.jms.*;
import java.util.HashMap;
import java.util.Map;

@Service
public class SQSService {

    @Autowired
    SQSConnectionFactory sqsConnectionFactory;

    public void setAsyncConsumer(final String queueName) {
        Session session = null;

        try {
            // Connection 생성하기.
            SQSConnection connection = sqsConnectionFactory.createConnection();

            makeQueue(connection, queueName);

            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Queue queue = session.createQueue(queueName);

            MessageConsumer consumer = session.createConsumer(queue);
            consumer.setMessageListener(new SQSMsgListener());
            connection.start();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(e);

            throw new RuntimeException(e);
        }
    }

    private void makeQueue(SQSConnection connection, String queueName) throws Exception {
        // Get the wrapped client
        AmazonSQSMessagingClientWrapper client = connection.getWrappedAmazonSQSClient();

        // Create an SQS queue named MyQueue, if it doesn't already exist
        if (!client.queueExists(queueName)) {
            client.createQueue(queueName);
        }
    }

}
