package com.schooldevops.sqs.jms;

import com.schooldevops.sqs.jms.services.SQSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Random;

@SpringBootApplication
public class JmsApplication implements CommandLineRunner {

	@Autowired
	SQSService sqsService;

	public static void main(String[] args) {
		SpringApplication.run(JmsApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		Random random = new Random(System.currentTimeMillis());
		String message = "Hello : " + random.nextInt(2048);

		String send = sqsService.send("my-todo2", message);
		System.out.println("Message : " + send);
	}
}
