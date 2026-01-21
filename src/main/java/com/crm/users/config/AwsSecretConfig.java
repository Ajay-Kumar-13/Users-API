package com.crm.users.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;

@Configuration
public class AwsSecretConfig {
    @Bean
    public SecretsManagerClient secretsManagerClient(){
        return SecretsManagerClient.builder()
                .region(Region.AP_SOUTH_1)
                .build();
    }
}
