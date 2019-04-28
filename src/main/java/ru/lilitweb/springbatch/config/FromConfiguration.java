package ru.lilitweb.springbatch.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "ru.lilitweb.springbatch.from",
        entityManagerFactoryRef = "fromEntityManagerFactory")
public class FromConfiguration {

}
