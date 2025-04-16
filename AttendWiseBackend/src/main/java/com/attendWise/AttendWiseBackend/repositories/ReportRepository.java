package com.attendWise.AttendWiseBackend.repositories;

import com.attendWise.AttendWiseBackend.entities.ReportEntity;
import com.attendWise.AttendWiseBackend.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<ReportEntity, Long> {
    List<ReportEntity> findByStudent(UserEntity user);
}
