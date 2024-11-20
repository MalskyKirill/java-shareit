package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UserDto {
    private Long id;
    @NotBlank(message = "user name can't be null, empty or blank")
    @Pattern(regexp = "\\S+", message = "user name mast contain no whitespace")
    private String name;
    @NotBlank(message = "email can't be null, empty or blank")
    @Email(message = "email should be valid")
    private String email;
}
