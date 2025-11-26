package com.crm.users.DTO;

import com.crm.users.model.Authorities;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class CreateUserResponse {
    private UUID id;
    private String username;
    private KeyValuePair role;
    private List<KeyValuePair> authorities;
}
