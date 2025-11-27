package com.crm.users.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class KeyValuePair {
    private UUID id;
    private String name;
}
