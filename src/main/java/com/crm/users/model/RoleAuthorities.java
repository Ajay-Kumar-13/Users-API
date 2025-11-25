package com.crm.users.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Data
@AllArgsConstructor
@Table("role_authorities")
public class RoleAuthorities {
    private UUID rid;
    private UUID aid;
}
