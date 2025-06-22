package com.telecomProject.CDR.EnrichedCDR.serilaization;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.telecomProject.CDR.model.CDR;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Deserializer;
import org.springframework.kafka.support.serializer.DeserializationException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CDRDeSerializer implements Deserializer<CDR> {
    private final ObjectMapper objectMapper;

    public CDRDeSerializer() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    }

    /**
     * Deserialize a record value from a byte array into a value or object.
     *
     * @param topic topic associated with the data
     * @param data  serialized bytes; may be null; implementations are recommended to handle null by returning a value or null rather than throwing an exception.
     * @return deserialized typed data; may be null
     */
    @Override
    public CDR deserialize(String topic, byte[] data) throws DeserializationException {
        try{
            if(data == null){
                throw new RuntimeException("data is null");
            }
            else{
                //log.info("Deserializing data");
                return objectMapper.readValue(new String(data, "UTF-8"), CDR.class);
            }
        }
        catch (Exception e){
            log.info("Error while deserializing data {}",e.getMessage());
            throw new DeserializationException("Error while deserializing data",data,true,e);
        }
    }
}
