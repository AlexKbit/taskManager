package com.taskmanager.app;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Launcher for spring boot application.
 */
@ComponentScan
@EnableScheduling
@SpringBootApplication
public class ApplicationLauncher extends SpringBootServletInitializer {

    /**
     * Start application.
     *
     * @param args arguments
     */
    public static void main(String... args) {
        SpringApplication.run(ApplicationLauncher.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(ApplicationLauncher.class);
    }
}
