package com.webapp.urlshortener.controller;
import org.apache.commons.validator.routines.UrlValidator;
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

    @PostMapping()
    public String create(String url) {
        UrlValidator urlValidator = new UrlValidator(new String[]{"http", "https"});
        if (urlValidator.isValid(url)) {
            return Hashing.murmur3_32().hashString(url, StandardCharsets.UTF_8).toString();
        }

        throw new RuntimeException("URL Invalid: " + url);
    }

    @GetMapping()
    public String get(String url) {
        return url;
    }
}
