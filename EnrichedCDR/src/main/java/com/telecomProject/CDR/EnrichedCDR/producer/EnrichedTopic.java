package com.telecomProject.CDR.EnrichedCDR.producer;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EnrichedTopic {
    @Bean
    public NewTopic newTopic() {
        return new NewTopic("enriched-cdr-topic", 3, (short) 1);
    }
}
