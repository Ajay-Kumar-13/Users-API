package com.crm.users.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
@Table("users")
public class User {
    @Id
    private UUID id;

    private String username;
    private String email;
    private String password;
    private UUID role_id;
    private boolean is_account_active;
    private LocalDateTime created_at;
}
