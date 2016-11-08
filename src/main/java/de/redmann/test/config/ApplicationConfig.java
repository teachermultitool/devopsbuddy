package de.redmann.test.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;

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
	@Value ("${aws.s3.profile}")
	private String awsProfileName;
	
	
	
	@Bean
	public AmazonS3Client s3Client()
	{
		AWSCredentials credentials = new ProfileCredentialsProvider(awsProfileName).getCredentials();
		
		AmazonS3Client s3Client = new AmazonS3Client(credentials);
		
		Region region = Region.getRegion(Regions.EU_CENTRAL_1);
		s3Client.setRegion(region);
		
		return s3Client;
	}
}
