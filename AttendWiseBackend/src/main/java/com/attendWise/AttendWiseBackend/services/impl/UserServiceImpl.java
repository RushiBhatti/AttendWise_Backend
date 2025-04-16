package com.attendWise.AttendWiseBackend.services.impl;

import com.attendWise.AttendWiseBackend.dto.SignUpDTO;
import com.attendWise.AttendWiseBackend.dto.UserDTO;
import com.attendWise.AttendWiseBackend.entities.UserEntity;
import com.attendWise.AttendWiseBackend.entities.enums.Role;
import com.attendWise.AttendWiseBackend.exceptions.ResourceNotFoundException;
import com.attendWise.AttendWiseBackend.repositories.UserRepository;
import com.attendWise.AttendWiseBackend.services.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username).orElseThrow(
                () -> new BadCredentialsException("User not found with email: " + username)
        );
    }

    @Override
    public UserDTO getCurrentUser() {
        UserEntity userEntity = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return modelMapper.map(userEntity, UserDTO.class);
    }

    @Override
    public UserDTO getUserById(Long id) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User not found with id: " + id)
        );

        return modelMapper.map(userEntity, UserDTO.class);
    }

    @Override
    public UserDTO getUserByEmail(String mailOfUser) {
        UserEntity userEntity = userRepository.findByEmail(mailOfUser).orElseThrow(
                () -> new ResourceNotFoundException("User not found with email: " + mailOfUser)
        );

        return modelMapper.map(userEntity, UserDTO.class);
    }

    @Override
    public UserDTO getUserByEnrollmentNumber(String enrollmentNumber) {
        UserEntity userEntity = userRepository.findByEnrollmentNumber(enrollmentNumber).orElseThrow(
                () -> new ResourceNotFoundException("Student not found with enrollment number: " + enrollmentNumber)
        );

        return modelMapper.map(userEntity, UserDTO.class);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository
                .findAll()
                .stream()
                .map((userEntity) -> modelMapper.map(userEntity, UserDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDTO> getAllStudents() {
        return userRepository
                .findByRole(Role.STUDENT)
                .stream()
                .map((userEntity) -> modelMapper.map(userEntity, UserDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDTO> getAllAdmins() {
        return userRepository
                .findByRole(Role.ADMIN)
                .stream()
                .map((userEntity) -> modelMapper.map(userEntity, UserDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO updateUserById(Long id, UserDTO inputUser) {
        UserEntity olderUser = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User not found with id: " + id)
        );

        inputUser.setStudentId(id);
        UserEntity inputUserEntity = modelMapper.map(inputUser, UserEntity.class);
        modelMapper.map(inputUserEntity, olderUser);
        UserEntity updatedUserEntity = userRepository.save(olderUser);

        return modelMapper.map(updatedUserEntity, UserDTO.class);
    }

    @Override
    public void deleteUserById(Long userId) {
        isUserExistsById(userId);
        userRepository.deleteById(userId);
    }

    @Override
    public void promoteUserToAdminById(Long userId) {
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User " +
                "not found with id: " + userId));

        userEntity.setRole(Role.ADMIN);
        userRepository.save(userEntity);
    }

    @Override
    public UserDTO createNewUser(UserDTO userDTO) {
        UserEntity userEntity = modelMapper.map(userDTO, UserEntity.class);
        return modelMapper.map(userRepository.save(userEntity), UserDTO.class);
    }

    @Override
    public UserDTO signUp(SignUpDTO signUpDTO) {
        Optional<UserEntity> userEntity = userRepository.findByEmail(signUpDTO.getEmail());
        if (userEntity.isPresent()){
            throw new BadCredentialsException("User already exists with email: " + signUpDTO.getEmail());
        }

        UserEntity toBeCreatedUser = modelMapper.map(signUpDTO, UserEntity.class);
        toBeCreatedUser.setPassword(passwordEncoder.encode(toBeCreatedUser.getPassword()));

        UserEntity savedUser = userRepository.save(toBeCreatedUser);
        return modelMapper.map(savedUser, UserDTO.class);
    }

    private void isUserExistsById(Long userId){
        if(!userRepository.existsById(userId)){
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }
    }
}
