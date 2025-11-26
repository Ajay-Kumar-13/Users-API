package com.crm.users.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;
@Data
@Table("roles")
public class Role {
    @Id
    @Column("role_id")
    private UUID roleId;

    @Column("role_name")
    private String roleName;
}
