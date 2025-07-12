package com.telecomProject.CDR.producer;

import com.telecomProject.CDR.model.CDR;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class CDRProducer {
    private static final Logger log = LoggerFactory.getLogger(CDRProducer.class);
    private final KafkaTemplate<String,CDR> kafkaTemplate;

    public CDRProducer(KafkaTemplate<String, CDR> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void produceCDR(CDR cdr) {
        log.trace("before sending message {}",cdr.caller());
        kafkaTemplate.send("cdr.topic", cdr.caller(),cdr);
        log.trace("after sending message {}",cdr.caller());
    }
}
