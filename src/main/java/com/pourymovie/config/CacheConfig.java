package com.pourymovie.config;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.pourymovie.util.PageDeserializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableCaching
public class CacheConfig {

  @Bean
  public RedisCacheManager cacheManager(
      RedisConnectionFactory connectionFactory, ObjectMapper springObjectMapper) {

    ObjectMapper cacheObjectMapper = springObjectMapper.copy();

    SimpleModule pageModule = new SimpleModule();
    PageDeserializer pageDeserializer = new PageDeserializer();

    pageModule.addDeserializer(Page.class, pageDeserializer);

    @SuppressWarnings({"unchecked", "rawtypes"})
    JsonDeserializer<PageImpl<?>> pageImplDeserializer = (JsonDeserializer) pageDeserializer;

    pageModule.addDeserializer(PageImpl.class, pageImplDeserializer);

    cacheObjectMapper.registerModule(pageModule);

    cacheObjectMapper.activateDefaultTyping(
        cacheObjectMapper.getPolymorphicTypeValidator(), ObjectMapper.DefaultTyping.NON_FINAL);

    GenericJackson2JsonRedisSerializer jsonSerializer =
        new GenericJackson2JsonRedisSerializer(cacheObjectMapper);

    RedisCacheConfiguration defaultCacheConfiguration =
        RedisCacheConfiguration.defaultCacheConfig()
            .disableCachingNullValues()
            .serializeKeysWith(
                RedisSerializationContext.SerializationPair.fromSerializer(
                    new StringRedisSerializer()))
            .serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(jsonSerializer));

    return RedisCacheManager.builder(connectionFactory)
        .cacheDefaults(defaultCacheConfiguration)
        .build();
  }
}
