package com.fullstack.Backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableAsync
@EnableCaching
@EnableTransactionManagement
public class BackendApplication {

    public static void main(String[] args) {

        try {
            SpringApplication app = new SpringApplication(BackendApplication.class);
            app.run(args);
        } catch(Throwable ex) {
            ex.printStackTrace();
        }
    }
}
