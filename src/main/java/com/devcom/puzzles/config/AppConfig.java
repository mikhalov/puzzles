package com.devcom.puzzles.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Configuration;

@Configuration
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AppConfig {
    static {
        nu.pattern.OpenCV.loadLocally();
    }

}
