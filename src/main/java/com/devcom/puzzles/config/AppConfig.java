package com.devcom.puzzles.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    static {
        nu.pattern.OpenCV.loadLocally();
    }

}
