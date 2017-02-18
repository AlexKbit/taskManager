package com.taskmanager.app;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Launcher for spring boot application.
 */
@SpringBootApplication
@ComponentScan("com.taskmanager")
public class ApplicationLauncher extends WebMvcConfigurerAdapter {

    /**
     * Start application.
     *
     * @param args arguments
     */
    public static void main(String... args) {
        SpringApplication.run(ApplicationLauncher.class, args);
    }

}
