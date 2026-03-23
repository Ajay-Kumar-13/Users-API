package com.crm.users.DTO;

import com.crm.users.model.Authority;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OverrideAuthority {

    @NotNull
    private Authority authorityName;

    @NotNull
    private boolean active;
}
