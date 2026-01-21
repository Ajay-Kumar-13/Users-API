package com.crm.users;

import com.crm.users.security.DBSecretService;
import com.crm.users.security.Model.DbSecret;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DbInitializer {

//    TODO: use this implementation "io.awspring.cloud:spring-cloud-aws-starter-secrets-manager" dependency to fetch the secrets directly using applicaiton.properties

    private final DBSecretService dbSecretService;
    private final String secretName;

    public DbInitializer(DBSecretService dbSecretService, @Value("${aws.secretname}") String secretName) {
        this.dbSecretService = dbSecretService;
        this.secretName = secretName;
    }

    @PostConstruct
    public void init() {
        try {
            DbSecret secret = dbSecretService.getDbSecret(secretName);

            System.setProperty("DB_USERNAME", secret.getUsername());
            System.setProperty("DB_PASSWORD", secret.getPassword());

        } catch (Exception ex) {
            throw new IllegalStateException(
                    "Failed to load DB credentials from AWS Secrets Manager", ex
            );
        }
    }
}
