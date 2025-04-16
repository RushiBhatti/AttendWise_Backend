package com.attendWise.AttendWiseBackend.services;

import com.attendWise.AttendWiseBackend.dto.AttendanceDTO;

import java.util.List;

public interface AttendanceService {

    AttendanceDTO getAttendanceById(Long id);

    List<AttendanceDTO> getAllAttendance();

    List<AttendanceDTO> getAllAttendanceByUserId(Long user_id);

    AttendanceDTO updateAttendanceById(Long id, AttendanceDTO updateAttendance);

    void deleteAttendanceById(Long id);

    void markAttendanceByQR(String enrollmentNumber, String sessionToken);

    void markAttendanceManually(Long studentId, Long sessionId);

    AttendanceDTO updateAttendanceStatus(Long attendanceId, String status);

    List<AttendanceDTO> getAllAttendanceBySessionId(Long sessionId);

    void markAbsentForRemainingStudents(Long sessionId);
}
