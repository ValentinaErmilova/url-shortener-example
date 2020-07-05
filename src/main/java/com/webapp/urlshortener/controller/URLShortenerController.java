package com.webapp.urlshortener.controller;
import com.webapp.urlshortener.config.SpringRedisConfig;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import com.google.common.hash.Hashing;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

/*
 ** GET - разворачивание ссылки
 ** POST - сворачивание ссылки
 */
@RestController
@RequestMapping("/")
public class URLShortenerController {
    private static final String REDIS_TEMPLATE = "redisTemplate";

    @Autowired
    private Environment env;

    @PostMapping()
    public String create(String url) throws UnknownHostException {
        UrlValidator urlValidator = new UrlValidator(new String[]{"http", "https"});
        if (urlValidator.isValid(url)) {
            ConfigurableApplicationContext ctx = new AnnotationConfigApplicationContext(SpringRedisConfig.class);
            String uuid = Hashing.murmur3_32().hashString(url, StandardCharsets.UTF_8).toString();
            RedisTemplate redisTemplate = (RedisTemplate) ctx.getBean(REDIS_TEMPLATE);
            redisTemplate.opsForValue().set(uuid, url);
            InetAddress.getLocalHost().getHostAddress();
            return String.format("http://%s:%s/%s", InetAddress.getLocalHost().getCanonicalHostName(), env.getProperty("server.port"), uuid);
        }

        throw new RuntimeException("URL Invalid: " + url);
    }

    @GetMapping("/{id}")
    public String get(@PathVariable String id) {
        ConfigurableApplicationContext ctx = new AnnotationConfigApplicationContext(SpringRedisConfig.class);
        RedisTemplate redisTemplate = (RedisTemplate) ctx.getBean(REDIS_TEMPLATE);
        String url = redisTemplate.opsForValue().get(id).toString();

        if (url == null) {
            throw new RuntimeException("There is no shorter URL for : " + id);
        }
        return url;
    }
}
