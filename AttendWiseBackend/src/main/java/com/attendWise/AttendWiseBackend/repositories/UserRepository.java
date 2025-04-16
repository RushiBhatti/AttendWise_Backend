package com.attendWise.AttendWiseBackend.repositories;

import com.attendWise.AttendWiseBackend.entities.UserEntity;
import com.attendWise.AttendWiseBackend.entities.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    List<UserEntity> findByRole(Role role);
    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByEnrollmentNumber(String enrollmentNumber);
}
