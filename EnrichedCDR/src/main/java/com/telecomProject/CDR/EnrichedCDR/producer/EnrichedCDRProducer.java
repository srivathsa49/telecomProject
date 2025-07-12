package com.telecomProject.CDR.EnrichedCDR.producer;

import com.telecomProject.CDR.EnrichedCDR.entity.EnrichedCDR;
import com.telecomProject.CDR.EnrichedCDR.feign.CustomerClient;
import com.telecomProject.CDR.model.CDR;
import com.telecomProject.CDR.model.Customer;
import net.datafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EnrichedCDRProducer {
    private static final Logger logger = LoggerFactory.getLogger(EnrichedCDRProducer.class);
    private static final List<String> PLANS = new ArrayList<>();
//    private static final List<String> states = List.of(
//            "Andhra Pradesh",
//            "Arunachal Pradesh",
//            "Assam",
//            "Bihar",
//            "Chhattisgarh",
//            "Goa",
//            "Gujarat",
//            "Haryana",
//            "Himachal Pradesh",
//            "Jharkhand",
//            "Karnataka",
//            "Kerala",
//            "Madhya Pradesh",
//            "Maharashtra",
//            "Manipur",
//            "Meghalaya",
//            "Mizoram",
//            "Nagaland",
//            "Odisha",
//            "Punjab",
//            "Rajasthan",
//            "Sikkim",
//            "Tamil Nadu",
//            "Telangana",
//            "Tripura",
//            "Uttar Pradesh",
//            "Uttarakhand",
//            "West Bengal"
//    );
    private final Faker faker = new Faker();
    @Autowired
    private KafkaTemplate<String, EnrichedCDR> kafkaTemplate;
    private CustomerClient customerClient;

    public EnrichedCDRProducer(CustomerClient customerClient) {
        this.customerClient = customerClient;
    }

    public void produce(CDR cdr) {
//        double callCost = cdr.callDuration() * 0.1; //it will be done in billing service
        String countryCode = "+91";
        String countryName = "India";
//        String state = faker.options().option(states.toArray(new String[0]));
        String uuid = cdr.uuid();
        Customer customer = customerClient.getCustomerById(uuid).getBody();
        String city = faker.address().city();
        String zipCode = faker.address().zipCode();
        String address = customer.getAddress();
        String email = customer.getEmail();
        EnrichedCDR enrichedCDR = EnrichedCDR.newBuilder()
                .setFirstName(customer.getFirstName())
                .setLastName(customer.getLastName())
                .setCaller(cdr.caller())
                .setReceiver(cdr.receiver())
                .setCallDuration(cdr.callDuration())
                .setCallDate(cdr.callDate())
                .setCallType(cdr.callType())
                .setIp(cdr.ip())
                .setCustPlan(customer.getPlan().getPlanName())
                .setCountryCode(countryCode)
                .setCountryName(countryName)
//                .setState(state)
//                .setCity(city)
//                .setZipCode(zipCode)
                .setAddress(address)
                .setEmail(email)
                .setUuid(uuid)
                .build();
        kafkaTemplate.send("enriched.cdr.topic", enrichedCDR.getCaller(), enrichedCDR);
    }
}
