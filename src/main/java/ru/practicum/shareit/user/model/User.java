package ru.practicum.shareit.user.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * TODO Sprint add-controllers.
 */

@Data
public class User {
    private Long id;
    @NotBlank(message = "user name can't be null, empty or blank")
    @Pattern(regexp = "\\S+", message = "user name mast contain no whitespace")
    private String name;
    @NotBlank(message = "email can't be null, empty or blank")
    @Email(message = "email should be valid")
    private String email;

}
