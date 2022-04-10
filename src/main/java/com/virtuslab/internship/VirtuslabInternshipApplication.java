package com.virtuslab.internship;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.virtuslab.internship"})
public class VirtuslabInternshipApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(VirtuslabInternshipApplication.class, args);
    }
}
