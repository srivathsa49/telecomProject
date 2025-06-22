package com.telecomProject.CDR.producer;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Topic {
    @Bean
    public NewTopic newTopic() {
        return new NewTopic("cdr-topic", 3,(short)1);
    }
}