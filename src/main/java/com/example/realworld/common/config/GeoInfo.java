package com.example.realworld.common.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
@Getter
public class GeoInfo {
    public static String API_KEY;


    @Autowired
    public void loadApiKey(@Value("${spring.geo.api-key}") String key) {
        API_KEY = key;
    }

}
