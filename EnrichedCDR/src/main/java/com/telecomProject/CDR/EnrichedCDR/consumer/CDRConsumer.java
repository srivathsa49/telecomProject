package com.telecomProject.CDR.EnrichedCDR.consumer;

import com.telecomProject.CDR.EnrichedCDR.producer.EnrichedCDRProducer;
import com.telecomProject.CDR.model.CDR;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
@Slf4j
@Component
public class CDRConsumer {
    @Autowired
    private EnrichedCDRProducer enrichedCDRProducer;

    @KafkaListener(topics = "cdr-topic")
    public void consume(@Header(KafkaHeaders.RECEIVED_KEY) Long caller, @Payload CDR cdr){
        log.info("Received Caller: {}, and CDR: {}", caller,cdr);
        enrichedCDRProducer.produce(cdr);
    }
}
