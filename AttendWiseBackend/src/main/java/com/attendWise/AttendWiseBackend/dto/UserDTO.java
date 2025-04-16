package com.attendWise.AttendWiseBackend.dto;

import com.attendWise.AttendWiseBackend.entities.enums.Role;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private Long studentId;

    @NotBlank(message = "First Name can not be blank!")
    private String firstName;

    @NotBlank(message = "Last Name can not be blank!")
    private String lastName;

    @NotBlank(message = "Email can not be blank!")
    @Email(message = "Email must be valid email!")
    private String email;

    @NotBlank(message = "Password can not be blank!")
    @Size(min = 6, message = "Minimum length of password can be 6 only")
    private String password;

    @NotBlank(message = "Enrollment Number can not be blank!")
    private String enrollmentNumber;

    @NotBlank(message = "Mobile Number can not be blank!")
    @Size(min = 10, max = 10, message = "Mobile Number must be of length 10.")
    private String mobileNumber;

    @NotBlank(message = "Branch can not be blank!")
    private String branch;

    @NotNull(message = "Semester can not be null!")
    @Min(value = 1, message = "Value of semester must be in the range 1 to 8")
    @Max(value = 8, message = "Value of semester must be in the range 1 to 8")
    private Integer semester;

//    @Pattern(regexp = "^(ADMIN|STUDENT)$", message = "Role of user can be 'STUDENT' or 'ADMIN' only")
    private Role role;  // STUDENT or ADMIN

    private LocalDate createdAt;
}
