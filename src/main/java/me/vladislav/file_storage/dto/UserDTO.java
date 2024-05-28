package me.vladislav.file_storage.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDTO {

    @NotBlank(message = "Login cannot be empty")
    @Size(min = 3, max = 50, message="Not valid size of login")
    private String login;

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 8, max = 64, message="Not valid size of password")
    private String password;

}
