package com.crm.users.model;

import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("user_authorities")
public class UserAuthority {
    @Column("authority_id")
    private UUID authorityId;

    private boolean active;
}
