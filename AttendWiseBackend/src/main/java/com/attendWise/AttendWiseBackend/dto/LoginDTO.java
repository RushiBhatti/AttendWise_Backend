package com.attendWise.AttendWiseBackend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginDTO {

    @NotBlank(message = "Email can not be blank!")
    @Email(message = "Email must be valid email!")
    private String email;

    @NotBlank(message = "Password can not be blank!")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;
}
