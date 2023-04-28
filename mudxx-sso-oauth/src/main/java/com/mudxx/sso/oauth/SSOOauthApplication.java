package com.mudxx.sso.oauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.mudxx")
public class SSOOauthApplication {
    public static void main(String[] args) {
        SpringApplication.run(SSOOauthApplication.class, args);
    }
}
