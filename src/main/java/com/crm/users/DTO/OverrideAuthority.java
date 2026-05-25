package com.crm.users.DTO;

import java.util.UUID;

import com.crm.users.model.Authority;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OverrideAuthority {

    private UUID authId;
    private Authority authorityName;

    @NotNull
    private boolean active;
}
