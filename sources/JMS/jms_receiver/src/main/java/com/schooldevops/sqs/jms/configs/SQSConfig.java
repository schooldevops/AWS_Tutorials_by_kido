package com.schooldevops.sqs.jms.configs;

import com.amazon.sqs.javamessaging.ProviderConfiguration;
import com.amazon.sqs.javamessaging.SQSConnectionFactory;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SQSConfig {

    @Bean
    public SQSConnectionFactory sqsConnectionFactory() {
        SQSConnectionFactory connectionFactory = new SQSConnectionFactory(
                new ProviderConfiguration(),
                AmazonSQSClientBuilder.standard().withEndpointConfiguration(
                        new AwsClientBuilder.EndpointConfiguration(
                                "https://sqs.ap-northeast-2.amazonaws.com/103382364946/my-todo2",
                                "ap-northeast-2")
                ).withCredentials(new ProfileCredentialsProvider("terraform_user"))
        );

        return connectionFactory;
    }
}
