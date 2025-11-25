package com.crm.users.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Data
@Table("authorities")
public class authorities {

    @Id
    private UUID authority_id;
    private String authority_name;
}
