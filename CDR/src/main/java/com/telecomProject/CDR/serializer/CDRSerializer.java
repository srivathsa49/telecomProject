package com.telecomProject.CDR.serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.telecomProject.CDR.model.CDR;
import org.apache.kafka.common.serialization.Serializer;

public class CDRSerializer implements Serializer<CDR> {
    private final ObjectMapper objectMapper;

    public CDRSerializer() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    }

    /**
     * Convert {@code data} into a byte array.
     *
     * @param topic topic associated with data
     * @param data  typed data
     * @return serialized bytes
     */
    @Override
    public byte[] serialize(String topic, CDR data) {
        try{
            return objectMapper.writeValueAsBytes(data);
        }catch (JsonProcessingException e){
            throw new RuntimeException("JSON serialization error", e);
        }
        catch (Exception e) {
            throw new RuntimeException("Exception during serialization error", e);
        }
    }
}
