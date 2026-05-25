package com.crm.users.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.UUID;

@Data
@Table(name = "refresh_tokens")
public class RefreshToken {
    @Id
    private UUID rt_id;

    private String token;
    @Column("user_id")
    private UUID userId;
    private Instant expiresAt;
}
