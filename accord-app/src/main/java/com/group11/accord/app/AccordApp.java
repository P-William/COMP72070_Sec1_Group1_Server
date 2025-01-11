package com.group11.accord.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan(basePackages = "com.group11.accord.*")
@ComponentScan(basePackages = "com.group11.accord.*")
@EnableJpaRepositories(basePackages = "com.group11.accord.*")
@SpringBootApplication
public class AccordApp {

    public static void main(String[] args) {
        SpringApplication.run(AccordApp.class, args);
    }
}
