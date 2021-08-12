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

    public String send(final String queueName, final String message) throws RuntimeException {

        Session session = null;

        try {
            // Connection 생성하기.
            SQSConnection connection = sqsConnectionFactory.createConnection();

            makeQueue(connection, queueName);
//            makeFifoQueue(connection, queueName);

            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Queue queue = session.createQueue(queueName);
            MessageProducer producer = session.createProducer(queue);

            TextMessage textMessage = session.createTextMessage(message);
            producer.send(textMessage);



        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(e);

            throw new RuntimeException(e);
        } finally {
            if (session != null) {
                try {
                    session.close();
                } catch (JMSException e) {
                    System.out.println(e.getMessage());
                    System.out.println(e);
                    throw new RuntimeException(e);
                }
            }
        }

        return message;
    }

    public String receive(final String queueName) throws RuntimeException {
        Session session = null;

        try {
            // Connection 생성하기.
            SQSConnection connection = sqsConnectionFactory.createConnection();

            makeQueue(connection, queueName);
//            makeFifoQueue(connection, queueName);

            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Queue queue = session.createQueue(queueName);
            // Create a consumer for the 'MyQueue'
            MessageConsumer consumer = session.createConsumer(queue);
            // Start receiving incoming messages
            connection.start();

            // Receive a message from 'MyQueue' and wait up to 1 second
            Message receivedMessage = consumer.receive(1000);

            // Cast the received message as TextMessage and display the text
            if (receivedMessage != null) {
                System.out.println("Received: " + ((TextMessage) receivedMessage).getText());
                return ((TextMessage) receivedMessage).getText();
            }
            return null;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(e);

            throw new RuntimeException(e);
        } finally {
            if (session != null) {
                try {
                    session.close();
                } catch (JMSException e) {
                    System.out.println(e.getMessage());
                    System.out.println(e);
                    throw new RuntimeException(e);
                }
            }
        }
    }

//    public void setAsyncConsumer(final String queueName) {
//        Session session = null;
//
//        try {
//            // Connection 생성하기.
//            SQSConnection connection = sqsConnectionFactory.createConnection();
//
//            makeQueue(connection, queueName);
//
//            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
//            Queue queue = session.createQueue(queueName);
//
//            MessageConsumer consumer = session.createConsumer(queue);
//            consumer.setMessageListener(new SQSMsgListener());
//            connection.start();
//
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//            System.out.println(e);
//
//            throw new RuntimeException(e);
//        }
//    }

    private void makeQueue(SQSConnection connection, String queueName) throws Exception {
        // Get the wrapped client
        AmazonSQSMessagingClientWrapper client = connection.getWrappedAmazonSQSClient();

        // Create an SQS queue named MyQueue, if it doesn't already exist
        if (!client.queueExists(queueName)) {
            client.createQueue(queueName);
        }
    }


    private void makeFifoQueue(SQSConnection connection, String queueName) throws Exception {
        // Get the wrapped client
        AmazonSQSMessagingClientWrapper client = connection.getWrappedAmazonSQSClient();

        String fQueueName = String.format("%s.fifo", queueName);
        // Create an Amazon SQS FIFO queue named MyQueue.fifo, if it doesn't already exist
        if (!client.queueExists(fQueueName)) {
            Map<String, String> attributes = new HashMap<String, String>();
            attributes.put("FifoQueue", "true");
            attributes.put("ContentBasedDeduplication", "true");
            client.createQueue(new CreateQueueRequest().withQueueName(fQueueName).withAttributes(attributes));
        }
    }
}
