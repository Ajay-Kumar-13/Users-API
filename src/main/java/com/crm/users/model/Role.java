package com.crm.users.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
@Table("roles")
public class Role {
    @Id
    private UUID id;

    private String roleName;

    private List<String> permissions;
}
