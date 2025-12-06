package com.crm.users.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LoginRequest {
    @NotNull
    String username;

    @NotNull
    String password;
}
