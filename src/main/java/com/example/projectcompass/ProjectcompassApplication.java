package com.example.projectcompass;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ProjectcompassApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjectcompassApplication.class, args);
    }

}
