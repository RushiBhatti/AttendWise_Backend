package com.attendWise.AttendWiseBackend.services;

import com.attendWise.AttendWiseBackend.dto.LoginDTO;
import com.attendWise.AttendWiseBackend.dto.LoginResponseDTO;
import com.attendWise.AttendWiseBackend.entities.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class Authservice {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public LoginResponseDTO login(LoginDTO loginDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword())
        );

        UserEntity user = (UserEntity) authentication.getPrincipal();

        String token = jwtService.generateTokenForUser(user);

        return new LoginResponseDTO(token);
    }

}
