package com.crm.users.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("user_authorities")
@Data
public class UserAuthority {

    @Id
    private UUID id;

    @Column("user_id")
    private UUID userId;

    @Column("authority_id")
    private UUID authorityId;

    private boolean active;

    public UserAuthority(UUID userId, UUID authorityId, boolean active) {
        this.userId = userId;
        this.authorityId = authorityId;
        this.active = active;
    }
}
