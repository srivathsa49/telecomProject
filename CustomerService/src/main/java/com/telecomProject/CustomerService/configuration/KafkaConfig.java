package com.telecomProject.CustomerService.configuration;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.Map;

@Configuration
public class KafkaConfig {
    @Autowired
    private KafkaProperties kafkaProperties;
    //creating topics
//    //Topic to send new cust data ==> need to modify it to send only some data
//    @Bean
//    public NewTopic custTopic() {
//        return new NewTopic("cust.topic", 3, (short) 1);
//    }

    //Topic to send new plan
    @Bean
    public NewTopic plansTopic() {
        return new NewTopic("plans.topic", 3, (short) 1);
    }

    //Topic to send only balance and phNo
    //Topic to publish that some fields of customer have been updated or new csutomer is created
    @Bean
    public NewTopic custUpdatedTopic(){
        return new NewTopic("cust.updated.topic", 3, (short) 1);
    }

//    producerfactory to make it possible to send customer objects or produce customer topics
//    @Bean
//    public ProducerFactory<String, Cust> customerInfoProducerFactory() {
//        Map<String, Object> config = kafkaProperties.buildProducerProperties();
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.registerModule(new JavaTimeModule());
//        objectMapper.disable(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS);
//        config.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, false);
//        return new DefaultKafkaProducerFactory<>(config);
//    }
//
//    producerkafkafactory to bind prod factory to template
//    @Bean(name = "customerKafkaTemplate")
//    public KafkaTemplate<String, Customer> customerkafkaTemplate() {
//        return new KafkaTemplate<>(customerProducerFactory());
//    }
//
//    producerfactory to make it possible to send plan objects
//    @Bean
//    public ProducerFactory<String, Plan> planProducerFactory() {
//        Map<String, Object> config = kafkaProperties.buildProducerProperties();
//        return new DefaultKafkaProducerFactory<>(config);
//    }
//
//    producerkafkafactory to bind prod factory to template
//    @Bean(name = "planKafkaTemplate")
//    public KafkaTemplate<String, Plan> planKafkaTemplate() {
//        return new KafkaTemplate<>(planProducerFactory());
//    }

//    @Bean
//    public ProducerFactory<Integer, String> customerInfoProducerFactory() {
//        Map<String, Object> config = kafkaProperties.buildProducerProperties();
//        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class);
//        return new DefaultKafkaProducerFactory<>(config);
//    }
//
//    @Bean(name = "customerInfoKafkaTemplate")
//    public KafkaTemplate<Integer, String> customerInfoKafkaTemplate() {
//        return new KafkaTemplate<>(customerInfoProducerFactory());
//    }
//
//    @Bean
//    public ProducerFactory<String, String> PlanProducerFactory() {
//        Map<String, Object> config = kafkaProperties.buildProducerProperties();
//        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//        return new DefaultKafkaProducerFactory<>(config);
//    }
//
//    @Bean(name = "planKafkaTemplate")
//    public KafkaTemplate<String, String> planKafkaTemplate() {
//        return new KafkaTemplate<>(PlanProducerFactory());
//    }

}
