package com.attendWise.AttendWiseBackend.services;

import com.attendWise.AttendWiseBackend.dto.SignUpDTO;
import com.attendWise.AttendWiseBackend.dto.UserDTO;

import java.util.List;

public interface UserService {
    UserDTO signUp(SignUpDTO signUpDTO);

    UserDTO getCurrentUser();

    UserDTO getUserById(Long id);

    UserDTO getUserByEmail(String mailOfUser);

    UserDTO getUserByEnrollmentNumber(String enrollmentNumber);

    List<UserDTO> getAllUsers();

    List<UserDTO> getAllStudents();

    List<UserDTO> getAllAdmins();

    UserDTO updateUserById(Long id, UserDTO inputUser);

    void deleteUserById(Long userId);

    void promoteUserToAdminById(Long userId);

    UserDTO createNewUser(UserDTO userDTO);
}
