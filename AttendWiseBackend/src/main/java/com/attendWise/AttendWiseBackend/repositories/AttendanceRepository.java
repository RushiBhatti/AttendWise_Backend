package com.attendWise.AttendWiseBackend.repositories;

import com.attendWise.AttendWiseBackend.entities.AttendanceEntity;
import com.attendWise.AttendWiseBackend.entities.SessionEntity;
import com.attendWise.AttendWiseBackend.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<AttendanceEntity, Long> {
    List<AttendanceEntity> findByStudentId(UserEntity user);

    List<AttendanceEntity> findBySessionId(SessionEntity session);

    List<AttendanceEntity> findByStatus(String status);

    boolean existsByStudentIdAndSessionId(UserEntity user, SessionEntity session);
}
