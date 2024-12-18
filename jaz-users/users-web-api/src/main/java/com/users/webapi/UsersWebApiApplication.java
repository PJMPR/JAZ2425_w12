package com.users.webapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.users")
public class UsersWebApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(UsersWebApiApplication.class, args);
    }

}
