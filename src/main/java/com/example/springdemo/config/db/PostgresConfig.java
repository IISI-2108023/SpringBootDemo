package com.example.springdemo.config.db;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(
        basePackages = {"com.example.springdemo.*.dao"},// package for repository
        entityManagerFactoryRef = "postgresEntityManagerFactory",
        transactionManagerRef = "postgresTransactionManager"
)
public class PostgresConfig {

    @Primary // 預設連線，只能設在一組datasource上
    @Bean("postgresDataSource")
    @ConfigurationProperties("spring.datasource.demo1")// prop prefix, or prefix=""
    public DataSource postgresDataSource() {
        return DataSourceBuilder.create().build();// [TODO] PGSimpleDataSource -> dataSource for postgres
    }

    @Primary
    @Bean("postgresEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean postgresEntityManagerFactory(
            @Qualifier("postgresDataSource") DataSource dataSource,
            JpaProperties jpaProperties
    ) {
        return createEntityManagerFactoryBuilder(jpaProperties)
                .dataSource(dataSource)
                .packages("com.example.springdemo.*.model")
                .persistenceUnit("pu-demo1")
                .build();
    }

    @Primary
    @Bean("postgresTransactionManager")
    public PlatformTransactionManager postgresTransactionManager(
            @Qualifier("postgresEntityManagerFactory") LocalContainerEntityManagerFactoryBean entityManagerFactory
    ) {
        return new JpaTransactionManager(entityManagerFactory.getObject());
    }

    // customize jpa property, or it will auto config "spring.jpa.*"
    @Bean
    @ConfigurationProperties("spring.jpa")
    public JpaProperties jpaProperties() {
        return new JpaProperties();
    }

    private JpaVendorAdapter createJpaVendorAdaptor(JpaProperties jpaProperties) {
        // another way to set spring.jpa.show-sql
//        AbstractJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
//        adapter.setShowSql(jpaProperties.isShowSql());
        return new HibernateJpaVendorAdapter();
    }

    private EntityManagerFactoryBuilder createEntityManagerFactoryBuilder(JpaProperties jpaProperties) {
        return new EntityManagerFactoryBuilder(createJpaVendorAdaptor(jpaProperties), jpaProperties.getProperties(), null);
    }

}
