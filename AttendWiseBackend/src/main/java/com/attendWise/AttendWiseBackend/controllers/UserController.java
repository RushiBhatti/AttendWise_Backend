package com.attendWise.AttendWiseBackend.controllers;

import com.attendWise.AttendWiseBackend.advices.ApiResponse;
import com.attendWise.AttendWiseBackend.dto.UserDTO;
import com.attendWise.AttendWiseBackend.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // Get current logged-in user info
    @GetMapping(path = "/me")
    public ResponseEntity<UserDTO> getLoggedInUser() {
        return ResponseEntity.ok(userService.getCurrentUser());
    }

    // Update user info
    @PutMapping(path = "/update/{userId}")
    public ResponseEntity<UserDTO> updateUserById(@PathVariable Long userId, @RequestBody @Valid UserDTO userDTO) {
        return ResponseEntity.ok(userService.updateUserById(userId, userDTO));
    }

    // Create User
    @PostMapping
    public ResponseEntity<UserDTO> createNewUser(@RequestBody @Valid UserDTO userDTO){
        return ResponseEntity.ok(userService.createNewUser(userDTO));
    }

    // Get user by ID (ADMIN ONLY)
    @GetMapping(path = "/{userId}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long userId){
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    // Get user by mail (ADMIN ONLY)
    @GetMapping(path = "/email/{email}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<UserDTO> getUserByEmail(@PathVariable String email){
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }

    // Get all users (ADMIN only)
    @GetMapping(path = "/all")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // Get All students (ADMIN ONLY)
    @GetMapping(path = "/students")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<List<UserDTO>> getAllStudents() {
        return ResponseEntity.ok(userService.getAllStudents());
    }

    // Get All Admins (ADMIN ONLY)
    @GetMapping(path = "/admins")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<List<UserDTO>> getAllAdmins() {
        return ResponseEntity.ok(userService.getAllAdmins());
    }

    // Delete user (ADMIN only)
    @DeleteMapping(path = "/{studentId}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<ApiResponse<String>> deleteUserById(@PathVariable Long studentId) {
        userService.deleteUserById(studentId);
        return ResponseEntity.ok(new ApiResponse<>("User deleted successfully"));
    }

    // Promote user to admin (ADMIN ONLY)
    @PostMapping(path = "/promote/{studentId}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<ApiResponse<String>> promoteUserToAdminById(@PathVariable Long studentId) {
        userService.promoteUserToAdminById(studentId);
        return ResponseEntity.ok(new ApiResponse<>("User promoted to ADMIN"));
    }
}
