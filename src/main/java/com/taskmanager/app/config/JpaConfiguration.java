package com.taskmanager.app.config;

import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Configuration for JPA
 */
@Configuration
@EntityScan("com.taskmanager")
@EnableJpaRepositories("com.taskmanager")
public class JpaConfiguration {

}
