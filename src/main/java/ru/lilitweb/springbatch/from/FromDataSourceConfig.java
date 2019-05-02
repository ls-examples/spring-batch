package ru.lilitweb.springbatch.from;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "fromEntityManagerFactory",
        transactionManagerRef = "fromTransactionManager",
        basePackages = { "ru.lilitweb.springbatch.from" }
)
public class FromDataSourceConfig {
    //база из которой будут мигрироваться данные
    @Bean
    @ConfigurationProperties(prefix="datasource.from")
    public DataSource fromDataSource() {
        return DataSourceBuilder.create()
                .build();
    }

    @Bean(name = "fromTransactionManager")
    public PlatformTransactionManager fromTransactionManager(
            @Qualifier("fromEntityManagerFactory") EntityManagerFactory
                    entityManagerFactory
    ) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean fromEntityManagerFactory(EntityManagerFactoryBuilder builder) {
        Map<String, Object> properties = new HashMap<String, Object>();
        properties.put("hibernate.hbm2ddl.auto", "create");
        return builder
                .dataSource(fromDataSource())
                .packages("ru.lilitweb.springbatch.from")
                .persistenceUnit("books")
                .properties(properties)
                .build();
    }
}
