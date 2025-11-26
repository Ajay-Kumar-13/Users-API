package com.crm.users.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class CreateAuthorityResponse {
    private UUID authorityId;
    private String authorityName;
}
