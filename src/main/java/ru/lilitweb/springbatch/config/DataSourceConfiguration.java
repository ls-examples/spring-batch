package ru.lilitweb.springbatch.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfiguration {

    //база из которой будут мигрироваться данные
    @Bean
    @ConfigurationProperties(prefix="datasource.from")
    public DataSource fromDataSource() {
        return DataSourceBuilder.create()
                .build();
    }

    //для сохранениея таблиц батча
    @Bean
    @Primary
    @ConfigurationProperties(prefix="datasource.primary")
    public DataSource primaryDataSource() {
        return DataSourceBuilder.create()
                .build();
    }


    @Bean
    public LocalContainerEntityManagerFactoryBean fromEntityManagerFactory(EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(fromDataSource())
                .packages("ru.lilitweb.springbatch.from")
                .persistenceUnit("books")
                .build();
    }
}
