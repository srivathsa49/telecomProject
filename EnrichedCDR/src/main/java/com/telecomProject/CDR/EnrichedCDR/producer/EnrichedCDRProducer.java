package com.telecomProject.CDR.EnrichedCDR.producer;

import com.telecomProject.CDR.EnrichedCDR.entity.EnrichedCDR;
import com.telecomProject.CDR.model.CDR;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnrichedCDRProducer {
    private static final List<String> PLANS = List.of("PREPAID", "POSTPAID", "CORPORATE","UNLIMITED","International");
    private static final List<String> states = List.of(
            "Andhra Pradesh",
            "Arunachal Pradesh",
            "Assam",
            "Bihar",
            "Chhattisgarh",
            "Goa",
            "Gujarat",
            "Haryana",
            "Himachal Pradesh",
            "Jharkhand",
            "Karnataka",
            "Kerala",
            "Madhya Pradesh",
            "Maharashtra",
            "Manipur",
            "Meghalaya",
            "Mizoram",
            "Nagaland",
            "Odisha",
            "Punjab",
            "Rajasthan",
            "Sikkim",
            "Tamil Nadu",
            "Telangana",
            "Tripura",
            "Uttar Pradesh",
            "Uttarakhand",
            "West Bengal"
    );
    private final Faker faker = new Faker();
    @Autowired
    private KafkaTemplate<Long, EnrichedCDR> kafkaTemplate;

    public void produce(CDR cdr) {
//        double callCost = cdr.callDuration() * 0.1; //it will be done in billing service
        String custPlan = faker.options().option(PLANS.toArray(new String[0]));
        String countryCode = "+91";
        String countryName = "India";
        String state = faker.options().option(states.toArray(new String[0]));
        String city = faker.address().city();
        String zipCode = faker.address().zipCode();
        String address = faker.address().fullAddress();
        String email = faker.internet().emailAddress();
        EnrichedCDR enrichedCDR = EnrichedCDR.newBuilder()
                .setFirstName(faker.name().firstName())
                .setLastName(faker.name().lastName())
                .setCaller(cdr.caller())
                .setReceiver(cdr.receiver())
                .setCallDuration(cdr.callDuration())
                .setCallDate(cdr.callDate())
                .setCallType(cdr.callType())
                .setIp(cdr.ip())
                .setCustPlan(custPlan)
                .setCountryCode(countryCode)
                .setCountryName(countryName)
                .setState(state)
                .setCity(city)
                .setZipCode(zipCode)
                .setAddress(address)
                .setEmail(email).build();
        kafkaTemplate.send("enriched-cdr-topic", enrichedCDR.getCaller(), enrichedCDR);
    }
}
