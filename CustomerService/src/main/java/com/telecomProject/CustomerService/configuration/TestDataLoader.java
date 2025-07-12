package com.telecomProject.CustomerService.configuration;

import com.telecomProject.CustomerService.entity.Customer;
import com.telecomProject.CustomerService.entity.Plan;
import com.telecomProject.CustomerService.repository.CustomerRepository;
import com.telecomProject.CustomerService.repository.PlanRepository;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import jakarta.annotation.PostConstruct;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

@Component
public class TestDataLoader {

    @Autowired private CustomerRepository customerRepository;
    @Autowired private PlanRepository planRepository;
    @Autowired private KafkaTemplate<String, String> customerInfoKafkaTemplate;
    @Autowired private KafkaTemplate<String, String> planKafkaTemplate;

    private final Faker faker = new Faker();
    private final Random random = new Random();

    @PostConstruct
    public void init() {
        List<Plan> plans = createPlans(); // Save and fetch list
        generateCustomers(plans);
    }

    private List<Plan> createPlans() {
        if (!planRepository.findAll().isEmpty()) return planRepository.findAll();
        List<Plan> plans = List.of(
                Plan.builder()
                        .planName("Super Saver 399")
                        .price(399)
                        .isInternational(false)
                        .details("""
This plan is perfect for light users. Enjoy 2GB data per day and 100 SMS daily. Ideal for students and budget-conscious users.
Voice calls to any network within India are unlimited. Basic caller tunes included.
""")
                        .ISD(0).LOCAL(1000).STD(100)
                        .offers("Free caller tune, 10GB bonus on recharge via app")
                        .planType("Prepaid").talkTime(500).dataPack(56).SMS(100)
                        .validity(28)
                        .build(),

                Plan.builder()
                        .planName("Unlimited India 699")
                        .price(699).isInternational(false)
                        .details("""
Experience truly unlimited calling to all networks across India. Includes 1.5GB/day high-speed data, then unlimited at reduced speed.
100 SMS/day and priority customer support access. App-exclusive contests and recharge cashback benefits included.
""")
                        .ISD(0).LOCAL(2000).STD(2000)
                        .offers("Disney+ Hotstar Mobile, free missed call alerts, premium ringtones")
                        .planType("Prepaid").talkTime(1000).dataPack(84).SMS(200)
                        .validity(56)
                        .build(),

                Plan.builder()
                        .planName("Global Talk 999")
                        .price(999).isInternational(true)
                        .details("""
Best suited for frequent international callers. Includes 100 ISD mins to US, UK, Singapore & Gulf countries.
National calls and data are unlimited. Complimentary international SMS pack and VoWiFi calling support.
""")
                        .ISD(100).LOCAL(500).STD(500)
                        .offers("Free 100 international mins, free SIM doorstep delivery, International Roaming Pack 50% off")
                        .planType("Postpaid").talkTime(1500).dataPack(30).SMS(100)
                        .validity(30)
                        .build(),

                Plan.builder()
                        .planName("Basic Talk 199")
                        .price(199).isInternational(false)
                        .details("""
Budget-friendly entry-level plan. Get 100 voice call minutes, 500MB of 4G data and 50 national SMS.
Suitable for feature phones and occasional users.
""")
                        .ISD(0).LOCAL(100).STD(50)
                        .offers("None")
                        .planType("Prepaid").talkTime(200).dataPack(7).SMS(50)
                        .validity(14)
                        .build(),

                Plan.builder()
                        .planName("Mega Data 799")
                        .price(799).isInternational(false)
                        .details("""
Designed for heavy data users. Get 3GB/day of blazing-fast 4G/5G data, unlimited calls, and 100 SMS/day.
Streaming, gaming, and remote work all included. No daily data cap on weekends.
""")
                        .ISD(0).LOCAL(2000).STD(2000)
                        .offers("Netflix Mobile Plan, weekend data rollover, 2-month music subscription free")
                        .planType("Postpaid").talkTime(2000).dataPack(84).SMS(250)
                        .validity(84)
                        .build(),

                Plan.builder()
                        .planName("Student Pack 299")
                        .price(299).isInternational(false)
                        .details("""
Specially curated for students. Includes 1.5GB/day data, free night-time high-speed data from 12AM to 6AM,
500 local/STD mins, and 100 SMS/day. Offers valid student discounts for future recharges.
""")
                        .ISD(0).LOCAL(500).STD(500)
                        .offers("10GB exam-time booster, referral rewards, free career app access")
                        .planType("Prepaid").talkTime(300).dataPack(28).SMS(100)
                        .validity(28)
                        .build(),

                Plan.builder()
                        .planName("Business ISD 1499")
                        .price(1499).isInternational(true)
                        .details("""
Tailored for global professionals. 200 ISD mins, 100GB data, 1000 STD/LOCAL mins, and access to business call management tools.
Also includes video conferencing data add-ons and customer success priority lane.
""")
                        .ISD(200).LOCAL(1000).STD(1000)
                        .offers("Zoom Pro Bundle, premium CRM integrations, free SIM replacements")
                        .planType("Postpaid").talkTime(3000).dataPack(90).SMS(300)
                        .validity(90)
                        .build(),

                Plan.builder()
                        .planName("Mini Plan 109")
                        .price(109).isInternational(false)
                        .details("""
Starter plan for minimal usage. Includes 100 mins of calling, 1GB high-speed data and 10 SMS.
Valid for 14 days. No daily limits, use as needed.
""")
                        .ISD(0).LOCAL(100).STD(10)
                        .offers("None")
                        .planType("Prepaid").talkTime(100).dataPack(14).SMS(10)
                        .validity(14)
                        .build(),

                Plan.builder()
                        .planName("Night Owl 209")
                        .price(209).isInternational(false)
                        .details("""
Best for night-time users. Unlimited local and STD calls from 11PM to 6AM. Daily 500MB high-speed data + bonus streaming data.
Free access to select OTT during night hours.
""")
                        .ISD(0).LOCAL(300).STD(300)
                        .offers("Free night streaming, bonus 2GB for night users, Spotify mini pack")
                        .planType("Prepaid").talkTime(500).dataPack(28).SMS(100)
                        .validity(28)
                        .build(),

                Plan.builder()
                        .planName("Weekend Booster 459")
                        .price(459).isInternational(false)
                        .details("""
Perfect for binge weekends. Regular data plus 5GB/weekend top-up. Unlimited calls & SMS. Recharge before Friday for auto boost.
Free access to games and weekend offers on food & travel apps.
""")
                        .ISD(0).LOCAL(1000).STD(1000)
                        .offers("Weekend data boost, travel coupon pack, 1-month game pass")
                        .planType("Prepaid").talkTime(1500).dataPack(56).SMS(150)
                        .validity(56)
                        .build(),

                // 🔽 NEWLY ADDED PLANS

                Plan.builder()
                        .planName("Silver Stream 599")
                        .price(599).isInternational(false)
                        .details("""
Balanced plan with 2GB/day, unlimited local/STD calls, 100 SMS/day. Great for work-from-home professionals.
""")
                        .ISD(0).LOCAL(1500).STD(1500)
                        .offers("Free caller tune, Amazon Prime Video (Mini)")
                        .planType("Postpaid").talkTime(1200).dataPack(60).SMS(150)
                        .validity(60)
                        .build(),

                Plan.builder()
                        .planName("Jet ISD 1999")
                        .price(1999).isInternational(true)
                        .details("""
Ultimate international roaming solution. 300 ISD mins, 200GB data, and unlimited national calling.
Best for business travelers and NRI use.
""")
                        .ISD(300).LOCAL(3000).STD(3000)
                        .offers("VoWiFi unlimited, international SIM swap support, Google One 200GB")
                        .planType("Postpaid").talkTime(5000).dataPack(120).SMS(400)
                        .validity(90)
                        .build(),

                Plan.builder()
                        .planName("Youth Buzz 189")
                        .price(189).isInternational(false)
                        .details("""
Short-term high-data booster. 2.5GB/day for 10 days with 500 mins calling and 50 SMS. Great for exam prep or travel.
""")
                        .ISD(0).LOCAL(500).STD(300)
                        .offers("Free night data, YouTube Learning Pack")
                        .planType("Prepaid").talkTime(400).dataPack(25).SMS(50)
                        .validity(10)
                        .build(),

                Plan.builder()
                        .planName("Family Combo 1299")
                        .price(1299).isInternational(false)
                        .details("""
Shared benefits for up to 4 family members. 200GB shared data, unlimited calls/SMS, parental control, family locator app.
""")
                        .ISD(0).LOCAL(4000).STD(4000)
                        .offers("Family OTT bundle, device security pack")
                        .planType("Postpaid").talkTime(4000).dataPack(200).SMS(500)
                        .validity(90)
                        .build(),

                Plan.builder()
                        .planName("Emergency Top-Up 59")
                        .price(59).isInternational(false)
                        .details("""
Emergency recharge plan. Valid for 2 days with 1GB data and 50 calling mins. Useful when balance is low.
""")
                        .ISD(0).LOCAL(50).STD(50)
                        .offers("None")
                        .planType("Prepaid").talkTime(100).dataPack(2).SMS(10)
                        .validity(2)
                        .build()
        );

        for (Plan plan : plans) {
            planKafkaTemplate.send("plans.topic", plan.getPlanName(), "new plan added planName {}"+ plan.getPlanName());
        }
        return planRepository.saveAll(plans);
    }

    private void generateCustomers(List<Plan> plans) {
        IntStream.range(0, 1000).forEach(i -> {
            Customer customer = new Customer();
            customer.setFirstName(faker.name().firstName());
            customer.setLastName(faker.name().lastName());
            customer.setAddress(faker.address().fullAddress());
            customer.setEmail(faker.internet().emailAddress());
            customer.setPhoneNumber(generateValidPhoneNumber());
            ZoneId indiaZone = ZoneId.of("Asia/Kolkata");
            LocalDateTime now = LocalDateTime.now(indiaZone);
            LocalDateTime startTime = now.minusDays(random.nextInt(21)) // 0 to 20 days ago
                    .withHour(random.nextInt(24))
                    .withMinute(random.nextInt(60))
                    .withSecond(random.nextInt(60));
            customer.setBalance(random.nextInt(300, 1000));
            customer.setActive(true);
            customer.setPlan(plans.get(random.nextInt(plans.size())));
            customer.setPlanStartTime(startTime);
            customer.setPlanEndTime(customer.getPlanStartTime().plusDays(customer.getPlan().getValidity()));
            Customer savedCustomer = customerRepository.save(customer);
            customerInfoKafkaTemplate.send("cust.updated.topic", savedCustomer.getId(), "new customer added");
        });
    }

    private String generateValidPhoneNumber() {
        PhoneNumberUtil util = PhoneNumberUtil.getInstance();
        String region = "IN";
        int countryCode = util.getCountryCodeForRegion(region);
        long number = 7_000_000_000L + random.nextInt(2_000_000_000); // 10-digit valid
        Phonenumber.PhoneNumber phone = new Phonenumber.PhoneNumber().setCountryCode(countryCode).setNationalNumber(number);

        if (util.isValidNumber(phone)) {
            return util.format(phone, PhoneNumberUtil.PhoneNumberFormat.E164);
        } else {
            Phonenumber.PhoneNumber fallback = util.getExampleNumberForType(region, PhoneNumberUtil.PhoneNumberType.MOBILE);
            return util.format(fallback, PhoneNumberUtil.PhoneNumberFormat.E164);
        }
    }
}

