package com.attendWise.AttendWiseBackend.services.impl;

import com.attendWise.AttendWiseBackend.dto.AttendanceDTO;
import com.attendWise.AttendWiseBackend.entities.AttendanceEntity;
import com.attendWise.AttendWiseBackend.entities.SessionEntity;
import com.attendWise.AttendWiseBackend.entities.UserEntity;
import com.attendWise.AttendWiseBackend.entities.enums.AttendanceStatus;
import com.attendWise.AttendWiseBackend.entities.enums.ModeOfMarkingAttendance;
import com.attendWise.AttendWiseBackend.exceptions.ResourceNotFoundException;
import com.attendWise.AttendWiseBackend.repositories.AttendanceRepository;
import com.attendWise.AttendWiseBackend.repositories.SessionRepository;
import com.attendWise.AttendWiseBackend.repositories.UserRepository;
import com.attendWise.AttendWiseBackend.services.AttendanceService;
import com.attendWise.AttendWiseBackend.services.JwtService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;
    private final ModelMapper modelMapper;
    private final JwtService jwtService;

    @Override
    public AttendanceDTO getAttendanceById(Long id) {
        AttendanceEntity attendanceEntity = attendanceRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Attendance not found with id: " + id)
        );

        return modelMapper.map(attendanceEntity, AttendanceDTO.class);
    }

    @Override
    public List<AttendanceDTO> getAllAttendance() {
        return attendanceRepository
                .findAll()
                .stream()
                .map((attendanceEntity -> modelMapper.map(attendanceEntity, AttendanceDTO.class)))
                .collect(Collectors.toList());
    }

    @Override
    public List<AttendanceDTO> getAllAttendanceByUserId(Long user_id) {

        UserEntity user = userRepository.findById(user_id).orElseThrow(() -> new ResourceNotFoundException("User " +
                "not found with id: " + user_id));

        return attendanceRepository
                .findByStudentId(user)
                .stream()
                .map((attendanceEntity -> modelMapper.map(attendanceEntity, AttendanceDTO.class)))
                .collect(Collectors.toList());
    }

    @Override
    public AttendanceDTO updateAttendanceById(Long id, AttendanceDTO updateAttendance) {

        AttendanceEntity olderAttendance = attendanceRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Attendance not found with id: " + id)
        );

        updateAttendance.setId(id);
        modelMapper.map(updateAttendance, olderAttendance);
        AttendanceEntity updatedAttendanceEntity = attendanceRepository.save(olderAttendance);

        return modelMapper.map(updatedAttendanceEntity, AttendanceDTO.class);
    }


    @Override
    public void deleteAttendanceById(Long id) {
        isAttendanceExistsById(id);
        attendanceRepository.deleteById(id);
    }

    @Override
    public void markAttendanceByQR(String enrollmentNumber, String sessionToken) {
        UserEntity userEntity = userRepository.findByEnrollmentNumber(enrollmentNumber).orElseThrow(
                () -> new ResourceNotFoundException("User not found with enrollment number: " + enrollmentNumber)
        );

        Long sessionId = jwtService.getSessionIdFromToken(sessionToken);

        SessionEntity sessionEntity = sessionRepository.findById(sessionId).orElseThrow(
                () -> new ResourceNotFoundException("Session not found with id: " + sessionId)
        );

        markAttendance(userEntity, sessionEntity, ModeOfMarkingAttendance.QR_SCAN);
    }

    @Override
    public void markAttendanceManually(Long studentId, Long sessionId) {
        UserEntity userEntity = userRepository.findById(studentId).orElseThrow(
                () -> new ResourceNotFoundException("User not found with id: " + studentId)
        );

        SessionEntity sessionEntity = sessionRepository.findById(sessionId).orElseThrow(
                () -> new ResourceNotFoundException("Session not found with id: " + sessionId)
        );

        markAttendance(userEntity, sessionEntity, ModeOfMarkingAttendance.MANUAL);
    }

    @Override
    public AttendanceDTO updateAttendanceStatus(Long attendanceId, String status) {
        AttendanceEntity attendanceEntity = attendanceRepository.findById(attendanceId).orElseThrow(
                () -> new ResourceNotFoundException("Attendance not found with id: " + attendanceId)
        );

        attendanceEntity.setStatus(AttendanceStatus.valueOf(status.toUpperCase()));

        return modelMapper.map(attendanceRepository.save(attendanceEntity), AttendanceDTO.class);
    }

    @Override
    public List<AttendanceDTO> getAllAttendanceBySessionId(Long sessionId) {

        SessionEntity session = sessionRepository.findById(sessionId).orElseThrow(
                () -> new ResourceNotFoundException("Session not found with id: " + sessionId)
        );

        return attendanceRepository
                .findBySessionId(session)
                .stream()
                .map((attendanceEntity -> modelMapper.map(attendanceEntity, AttendanceDTO.class)))
                .collect(Collectors.toList());
    }

    @Override
    public void markAbsentForRemainingStudents(Long sessionId) {
        SessionEntity session = sessionRepository.findById(sessionId).orElseThrow(
                () -> new ResourceNotFoundException("Session not found with id: " + sessionId)
        );

        List<UserEntity> allUsers = userRepository.findAll();
        List<UserEntity> presentUsers = attendanceRepository
                .findBySessionId(session)
                .stream()
                .map((AttendanceEntity::getStudentId))
                .toList();

        for(UserEntity user: allUsers){
            if(!presentUsers.contains(user)){
                AttendanceEntity attendanceEntity = new AttendanceEntity();
                attendanceEntity.setStudentId(user);
                attendanceEntity.setSessionId(session);
                attendanceEntity.setStatus(AttendanceStatus.ABSENT);
                attendanceEntity.setModeOfMarkingAttendance(ModeOfMarkingAttendance.MANUAL);

                attendanceRepository.save(attendanceEntity);
            }
        }
    }

    private void isAttendanceExistsById(Long id){
        if(!attendanceRepository.existsById(id)){
            throw new ResourceNotFoundException("Attendance not found with id: " + id);
        }
    }

    private void markAttendance(UserEntity userEntity, SessionEntity sessionEntity, ModeOfMarkingAttendance modeOfMarkingAttendance){
        // check if session is expired or not
        if(sessionEntity.getExpiresAt().isBefore(LocalDateTime.now())){
            throw new RuntimeException("QR Code (Session) has expired.");
        }

        // Check if attendance is already marked or not
        if (attendanceRepository.existsByStudentIdAndSessionId(userEntity, sessionEntity)) {
            throw new RuntimeException("Attendance already marked for this session.");
        }

        AttendanceEntity attendanceEntity = new AttendanceEntity();
        attendanceEntity.setStudentId(userEntity);
        attendanceEntity.setSessionId(sessionEntity);
        attendanceEntity.setStatus(AttendanceStatus.PRESENT);
        attendanceEntity.setModeOfMarkingAttendance(modeOfMarkingAttendance);

        attendanceRepository.save(attendanceEntity);
    }
}
