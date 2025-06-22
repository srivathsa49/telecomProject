package com.telecomProject.CDR.generator;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.telecomProject.CDR.model.CDR;
import com.telecomProject.CDR.producer.CDRProducer;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Slf4j
@Component
public class CDRGenerator {

    private final CDRProducer cdrProducer;

    public CDRGenerator(CDRProducer cdrProducer) {
        this.cdrProducer = cdrProducer;
    }
    private final Faker faker = new Faker();
    private final Random random = new Random();

    @Scheduled(fixedRate = 10)
    public void generateCDR() {
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        String callType = faker.random().nextBoolean() ? "Incoming" : "Outgoing";
        String callDirection = callType.equals("Incoming") ? "Inbound" : "Outbound";
        CallerReceiver callerReceiver = new CallerReceiver();
        callerReceiver.generateCallerReceiver(callType.equals("Incoming") ? 2:1);
        Long caller = callerReceiver.caller;
        Long receiver = callerReceiver.receiver;
        log.info("Caller: {}, Receiver: {}", caller, receiver);
        int callDuration = random.nextInt(600);
        Instant callDate = faker.timeAndDate().birthday()
                .atTime(random.nextInt(24), random.nextInt(60), random.nextInt(60))
                .atZone(ZoneId.of("Asia/Kolkata"))
                .toInstant();
        log.info("Call Date: {}", callDate);
        String ip = faker.internet().ipV4Address();

        CDR cdr = new CDR(firstName, lastName, caller, receiver, callDuration, callType, callDirection, callDate, ip);
        log.debug("In Generator before send {}", cdr.caller());
        cdrProducer.produceCDR(cdr);
    }

    private static class CallerReceiver {
        final PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
        final Random random = new Random();
        final List<String> regions = new ArrayList<>(phoneNumberUtil.getSupportedRegions());
        long caller;
        long receiver;
        private void generateCallerReceiver(int choice) {
            switch (choice) {
                case 1:
                    caller = generateValidNumber("IN");
                    receiver = generateValidNumber(null);
                    break;
                case 2:
                    caller = generateValidNumber(null);
                    receiver = generateValidNumber("IN");
                    break;
                default:
                    break;
            }
        }

        private long generateValidNumber(String region) {
            String regionCode = region != null ? region : regions.get(random.nextInt(regions.size()));
            int countryCode = phoneNumberUtil.getCountryCodeForRegion(regionCode);
            long national = (long)(Math.pow(10, 7) + random.nextInt(100_000_000));
            Phonenumber.PhoneNumber number = new Phonenumber.PhoneNumber()
                    .setCountryCode(countryCode)
                    .setNationalNumber(national);
            if (phoneNumberUtil.isValidNumber(number)) {
                return Long.parseLong(phoneNumberUtil.format(number, PhoneNumberUtil.PhoneNumberFormat.E164));
            }else {
                Phonenumber.PhoneNumber dummyNumber = PhoneNumberUtil.getInstance().getExampleNumberForType(regionCode, PhoneNumberUtil.PhoneNumberType.MOBILE);
                return Long.parseLong(phoneNumberUtil.format(dummyNumber, PhoneNumberUtil.PhoneNumberFormat.E164));
            }
        }
    }
}
