package com.crm.users.model;

import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("user_authorities")
public class UserAuthority {
    private UUID authority_id;
    private boolean active;
}
