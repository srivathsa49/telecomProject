package com.telecomProject.CDR.generator;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.telecomProject.CDR.feign.CustomerClient;
import com.telecomProject.CDR.model.CDR;
import com.telecomProject.CDR.model.Customer;
import com.telecomProject.CDR.producer.CDRProducer;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

@Slf4j
@Component
public class CDRGenerator {

    private final CDRProducer cdrProducer;
    private List<Customer> customers = Collections.synchronizedList(new ArrayList<>());
    private final PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
    private final List<String> regions = new ArrayList<>(phoneNumberUtil.getSupportedRegions());
    private CustomerClient customerClient;

    public CDRGenerator(CDRProducer cdrProducer, CustomerClient customerClient) {
        this.customerClient = customerClient;
        this.cdrProducer = cdrProducer;
    }
    private final Faker faker = new Faker();
    private final Random random = new Random();


    public void generateCDR() {
        Customer customer = customers.get(random.nextInt(customers.size()));
        String firstName = customer.getFirstName();
        String lastName = customer.getLastName();
        String callType = faker.random().nextBoolean() ? "Incoming" : "Outgoing";
        String callDirection = callType.equals("Incoming") ? "Inbound" : "Outbound";
        String caller;
        String receiver;
        String uuid = customer.getId();
        if(callType.equals("Incoming")) {
            receiver = customer.getPhoneNumber();
            caller = generateValidNumber();
        }
        else{
            receiver = generateValidNumber();
            caller = customer.getPhoneNumber();
        }
        log.info("Caller: {}, Receiver: {}", caller, receiver);
        int callDuration = random.nextInt(600);
        Instant callDate = randomBetween(customer.getPlanStartTime(),customer.getPlanEndTime()).atZone(ZoneId.systemDefault()).toInstant();
        log.info("Call Date: {}", callDate);
        String ip = faker.internet().ipV4Address();
        CDR cdr = new CDR(firstName, lastName, caller, receiver, callDuration, callType, callDirection, callDate, ip,uuid);
        log.debug("In Generator before send caller {}, uuid is {}", cdr.caller(),cdr.uuid());
        log.info("cdr is {}", cdr);
        cdrProducer.produceCDR(cdr);
    }

    private String generateValidNumber() {
        String regionCode = regions.get(random.nextInt(regions.size()));
        int countryCode = phoneNumberUtil.getCountryCodeForRegion(regionCode);
        long national = (long)(Math.pow(10, 7) + random.nextInt(100_000_000));
        Phonenumber.PhoneNumber number = new Phonenumber.PhoneNumber()
                .setCountryCode(countryCode)
                .setNationalNumber(national);
        if (phoneNumberUtil.isValidNumber(number)) {
            return phoneNumberUtil.format(number, PhoneNumberUtil.PhoneNumberFormat.E164);
        }else {
            Phonenumber.PhoneNumber dummyNumber = PhoneNumberUtil.getInstance().getExampleNumberForType(regionCode, PhoneNumberUtil.PhoneNumberType.MOBILE);
            if (dummyNumber != null && phoneNumberUtil.isValidNumber(dummyNumber)) {
                return phoneNumberUtil.format(dummyNumber, PhoneNumberUtil.PhoneNumberFormat.E164);
            } else {
                // Hardcoded emergency fallback
                return "+917700900000"; // Valid-looking UK test number
            }
        }
    }

    @KafkaListener(topics = "cust.updated.topic", groupId = "cdr-group1")
    public void consume(@Header(KafkaHeaders.RECEIVED_KEY) String uuid, String message) {
        Customer customer = customerClient.getCustomerById(uuid).getBody();
        customers.add(customer);
        log.info("Id is {}, Message is {} cust.uuid is {}",uuid, message, customer.getId());
        log.info("list of customers size is {}", customers.size());
        if(customers.size() == 1000) {
            log.info("All 1000 customers received. Starting bulk CDR generation...");
            new Thread(this::generateBulkCDRs).start();  // ✅ Run in a separate thread
        }
    }

    private void generateBulkCDRs() {
        List<CompletableFuture<Void>> futures = IntStream.range(0, 1000)
                .mapToObj(i -> CompletableFuture.runAsync(this::generateCDR))
                .toList();

        // Wait for all to finish
        log.info("waiting for all to finish and size is {}", futures.size());
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
    }


    public static LocalDateTime randomBetween(LocalDateTime start, LocalDateTime end) {
        ZoneOffset indianOffSet = ZoneOffset.ofHoursMinutes(5,30);
        long startEpoch = start.toEpochSecond(indianOffSet);
        long endEpoch = end.toEpochSecond(indianOffSet);
        long randomEpoch = ThreadLocalRandom.current().nextLong(startEpoch, endEpoch);

        return LocalDateTime.ofEpochSecond(randomEpoch, 0, indianOffSet);
    }

}
