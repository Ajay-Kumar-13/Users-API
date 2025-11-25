package com.crm.users.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Table("users")
public class User {
    @Id
    private UUID id;

    private String name;
    private UUID role_id;
    private LocalDateTime created_at;

    @MappedCollection(idColumn = "id")
    private Set<UserAuthority> authorities;
}
