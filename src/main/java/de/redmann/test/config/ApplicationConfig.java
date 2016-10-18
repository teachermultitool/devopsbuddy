package de.redmann.test.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by redmann on 17.10.16.
 */
@Configuration
@EnableJpaRepositories (basePackages = "de.redmann.test.backend.persistence.repositories")
@EntityScan (basePackages = "de.redmann.test.backend.persistence.domain.backend")
@PropertySource ("file:///${user.home}/.devopsbuddy/application-common.properties")
@EnableTransactionManagement
public class ApplicationConfig
{
}
