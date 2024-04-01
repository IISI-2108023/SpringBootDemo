package com.example.springdemo.config.db;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(
        basePackages = {"com.example.springdemo.persistence2.dao"},
        entityManagerFactoryRef = "secondEntityManagerFactory",
        transactionManagerRef = "secondTransactionManager"
)
public class SecondConfig {

    @Bean("secondDataSource")
    @ConfigurationProperties("spring.datasource.demo2")
    public DataSource secondDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean("secondEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean secondEntityManagerFactory(
            @Qualifier("secondDataSource") DataSource dataSource,
            EntityManagerFactoryBuilder builder
    ) {
        return builder.dataSource(dataSource)
                .packages("com.example.springdemo.persistence2.model")
                .persistenceUnit("pu-demo2")
                .build();
    }

    @Bean("secondTransactionManager")
    public PlatformTransactionManager secondTransactionManager(
            @Qualifier("secondEntityManagerFactory") LocalContainerEntityManagerFactoryBean entityManagerFactoryBean
    ) {
        return new JpaTransactionManager(entityManagerFactoryBean.getObject());
    }

    @Bean(name = "secondJdbcTemplate")
    public JdbcTemplate secondTemplate(@Qualifier("secondDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
