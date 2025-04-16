package com.attendWise.AttendWiseBackend.controllers;

import com.attendWise.AttendWiseBackend.dto.LoginDTO;
import com.attendWise.AttendWiseBackend.dto.LoginResponseDTO;
import com.attendWise.AttendWiseBackend.dto.SignUpDTO;
import com.attendWise.AttendWiseBackend.dto.UserDTO;
import com.attendWise.AttendWiseBackend.services.Authservice;
import com.attendWise.AttendWiseBackend.services.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final Authservice authservice;

    @PostMapping(path = "/signup")
    public ResponseEntity<UserDTO> signUp(@RequestBody @Valid SignUpDTO signUpDTO){
        UserDTO userDTO = userService.signUp(signUpDTO);
        return ResponseEntity.ok(userDTO);
    }

    @PostMapping(path = "/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginDTO loginDTO, HttpServletResponse response){
       LoginResponseDTO loginResponseDTO = authservice.login(loginDTO);

       Cookie cookie = new Cookie("token", loginResponseDTO.getToken());
       cookie.setHttpOnly(true);
       response.addCookie(cookie);

       return ResponseEntity.ok(loginResponseDTO);
    }

}
