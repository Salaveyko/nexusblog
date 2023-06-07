package com.nexusblog.nexusblog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@ComponentScan("com.nexusblog")
@EntityScan("com.nexusblog.persistence.entity")
@EnableJpaRepositories("com.nexusblog.persistence.repository")
public class NexusblogApplication {

	public static void main(String[] args) {
		SpringApplication.run(NexusblogApplication.class, args);
	}

}
