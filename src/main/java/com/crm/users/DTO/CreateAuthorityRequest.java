package com.crm.users.DTO;

import lombok.Data;

import java.util.UUID;

@Data
public class CreateAuthorityRequest {
    private UUID authorityId;
    private String authorityName;
}
