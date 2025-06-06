package redis.example.demo.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
@EnableCaching
public class RedisConfig {
    private static final Logger logger = LoggerFactory.getLogger(RedisConfig.class);

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        try {
            RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
            config.setHostName("localhost");
            config.setPort(6379);
            config.setPassword(""); // Nếu có password thì thêm vào đây
            
            LettuceConnectionFactory factory = new LettuceConnectionFactory(config);
            factory.afterPropertiesSet();
            
            // Test connection
            factory.getConnection().ping();
            logger.info("Successfully connected to Redis");
            
            return factory;
        } catch (Exception e) {
            logger.error("Failed to connect to Redis: {}", e.getMessage());
            throw new RuntimeException("Failed to connect to Redis", e);
        }
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        try {
            RedisTemplate<String, Object> template = new RedisTemplate<>();
            template.setConnectionFactory(connectionFactory);
            template.setKeySerializer(new StringRedisSerializer());
            template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
            template.setHashKeySerializer(new StringRedisSerializer());
            template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
            template.afterPropertiesSet();
            
            // Test template
            template.opsForValue().set("test", "test");
            template.delete("test");
            logger.info("RedisTemplate configured successfully");
            
            return template;
        } catch (Exception e) {
            logger.error("Failed to configure RedisTemplate: {}", e.getMessage());
            throw new RuntimeException("Failed to configure RedisTemplate", e);
        }
    }

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        try {
            RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                    .entryTtl(Duration.ofMinutes(60))
                    .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                    .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()))
                    .disableCachingNullValues();

            RedisCacheManager cacheManager = RedisCacheManager.builder(connectionFactory)
                    .cacheDefaults(config)
                    .build();
            
            logger.info("RedisCacheManager configured successfully");
            return cacheManager;
        } catch (Exception e) {
            logger.error("Failed to configure RedisCacheManager: {}", e.getMessage());
            throw new RuntimeException("Failed to configure RedisCacheManager", e);
        }
    }
} 