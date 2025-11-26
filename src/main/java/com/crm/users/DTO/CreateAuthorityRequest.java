package com.crm.users.DTO;

import lombok.Data;

import java.util.UUID;

@Data
public class CreateAuthorityRequest {
    private UUID authority_id;
    private String authority_name;
}
