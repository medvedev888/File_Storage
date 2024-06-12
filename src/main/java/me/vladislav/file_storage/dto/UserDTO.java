package me.vladislav.file_storage.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDTO {

    @NotBlank(message = "Login cannot be empty.")
    @Size(min = 3, max = 50, message="Not valid size of login.")
    @Pattern(regexp = "^[a-zA-Z0-9._-]*$", message = "Login must contain only alphanumeric characters and \'.\', \'-\', \'_\'.")
    private String login;

    @NotBlank(message = "Password cannot be empty.")
    @Size(min = 8, max = 64, message="Not valid size of password.")
    @Pattern(regexp = "^[a-zA-Z0-9._-]*$", message = "Password must contain only alphanumeric characters and \'.\', \'-\', \'_\'.")
    private String password;

}
