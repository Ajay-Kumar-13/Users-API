package com.crm.users.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RefreshToken {
    private String refreshToken;
    private CreateUserResponse user;
}
