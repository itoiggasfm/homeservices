package com.maktabsharif.homeservices;

import com.maktabsharif.homeservices.domain.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HomeservicesApplication {

    public static void main(String[] args) {
        SpringApplication.run(HomeservicesApplication.class, args);
        new User();
    }

}
