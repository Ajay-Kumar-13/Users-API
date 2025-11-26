package com.crm.users.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Data
@Table("authorities")
public class Authorities {

    @Id
    @Column("authority_id")
    private UUID authorityId;

    @Column("authority_name")
    private String authorityName;
}
