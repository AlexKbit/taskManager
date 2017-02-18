package com.taskmanager.app.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Configuration for JPA
 */
@Configuration
@EnableJpaRepositories("com.taskmanager")
public class JpaConfiguration {

}
