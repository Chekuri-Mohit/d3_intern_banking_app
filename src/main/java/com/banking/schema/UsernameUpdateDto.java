package com.banking.schema;

import jakarta.validation.constraints.AssertTrue;
import lombok.Data;

@Data
public class UsernameUpdateDto {
    private String currentPassword;
    private String newUsername;
    private String confirmUsername;
    @AssertTrue(message = "Both Usernames must match")
    public boolean isUsernameMatch() {
        return newUsername != null && newUsername.equals(confirmUsername);
    }
}

