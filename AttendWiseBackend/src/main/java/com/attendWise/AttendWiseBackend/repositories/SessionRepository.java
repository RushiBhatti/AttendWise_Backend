package com.attendWise.AttendWiseBackend.repositories;

import com.attendWise.AttendWiseBackend.entities.SessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionRepository extends JpaRepository<SessionEntity, Long> {
}
