package io.bootify.swetube.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@Configuration
@EntityScan("io.bootify.swetube.domain")
@EnableJpaRepositories("io.bootify.swetube.repos")
@EnableTransactionManagement
public class DomainConfig {
}
