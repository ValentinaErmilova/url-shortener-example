package com.webapp.urlshortener.controller;
import com.webapp.urlshortener.config.SpringRedisConfig;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;

/*
** GET - разворачивание ссылки
** POST - сворачивание ссылки
 */
@RestController
@RequestMapping("/")
public class URLShortenerController {
    private static final String REDIS_TEMPLATE = "redisTemplate";

    @PostMapping()
    public String create(String url) {
        UrlValidator urlValidator = new UrlValidator(new String[]{"http", "https"});
        if (urlValidator.isValid(url)) {
            ConfigurableApplicationContext ctx = new AnnotationConfigApplicationContext(SpringRedisConfig.class);
            String uuid = Hashing.murmur3_32().hashString(url, StandardCharsets.UTF_8).toString();
            RedisTemplate redisTemplate = (RedisTemplate) ctx.getBean(REDIS_TEMPLATE);
            redisTemplate.opsForValue().set(uuid, url);
            return uuid;
        }

        throw new RuntimeException("URL Invalid: " + url);
    }

    @GetMapping("/{id}")
    public String get(@PathVariable String id) {
        ConfigurableApplicationContext ctx = new AnnotationConfigApplicationContext(SpringRedisConfig.class);
        RedisTemplate redisTemplate = (RedisTemplate) ctx.getBean(REDIS_TEMPLATE);
        String url = redisTemplate.opsForValue().get(id).toString();
        System.out.println("URL Retrieved: " + url);

        if (url == null) {
            throw new RuntimeException("There is no shorter URL for : " + id);
        }
        return url;
    }
}
