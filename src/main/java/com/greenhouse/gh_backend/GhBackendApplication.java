package com.greenhouse.gh_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication(scanBasePackages = "com.greenhouse.gh_backend")

public class GhBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(GhBackendApplication.class, args);
    }

}
