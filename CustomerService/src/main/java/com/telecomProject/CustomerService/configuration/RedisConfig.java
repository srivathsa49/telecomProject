package com.telecomProject.CustomerService.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.telecomProject.CustomerService.entity.Customer;
import com.telecomProject.CustomerService.entity.Plan;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableCaching
public class RedisConfig {
//    @Bean
//    public RedisTemplate<String, Customer> redisTemplate(RedisConnectionFactory connectionFactory) {
//        RedisTemplate<String, Customer> template = new RedisTemplate<>();
//        template.setConnectionFactory(connectionFactory);
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.registerModule(new JavaTimeModule());
//        objectMapper.disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
//        GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer(objectMapper);
//
//        template.setKeySerializer(RedisSerializer.string());
//        template.setValueSerializer(serializer);
//        template.setHashKeySerializer(RedisSerializer.string());
//        template.setHashValueSerializer(serializer);
//        template.afterPropertiesSet();
//        return template;
//    }

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        Jackson2JsonRedisSerializer<Customer> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(mapper,Customer.class);
        Jackson2JsonRedisSerializer<Plan> jackson2JsonRedisSerializerPlan = new Jackson2JsonRedisSerializer<>(mapper,Plan.class);
        RedisCacheConfiguration redisCacheConfigurationBase = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(10));

        Map<String,RedisCacheConfiguration> initialCacheConfigurations = new HashMap<>();
        initialCacheConfigurations.put("CustomerCache", redisCacheConfigurationBase.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jackson2JsonRedisSerializer)));
        initialCacheConfigurations.put("planCache", redisCacheConfigurationBase.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jackson2JsonRedisSerializerPlan)));
        return RedisCacheManager.builder(connectionFactory)
                .withInitialCacheConfigurations(initialCacheConfigurations)
                .cacheDefaults(redisCacheConfigurationBase)
                .build();
    }

}
