package me.vladislav.file_storage.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDTO {
    @NotEmpty(message = "Login cannot be empty")
    @Size(min = 3, max = 50, message="Not valid size of login")
    private String login;
    @NotEmpty(message = "Password cannot be empty")
    @Size(min = 8, max = 64, message="Not valid size of password")
    private String password;
}
