package com.attendWise.AttendWiseBackend.controllers;

import com.attendWise.AttendWiseBackend.advices.ApiResponse;
import com.attendWise.AttendWiseBackend.dto.AttendanceDTO;
import com.attendWise.AttendWiseBackend.services.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    // Get attendance history of a student
    @GetMapping(path = "/student/{studentId}")
    public ResponseEntity<List<AttendanceDTO>> getAllAttendanceByUserId(@PathVariable Long studentId) {
        return ResponseEntity.ok(attendanceService.getAllAttendanceByUserId(studentId));
    }

    // Mark attendance using QR code
    @PostMapping(path = "/mark")
    public ResponseEntity<ApiResponse<String>> markAttendanceViaQR(
            @RequestParam String enrollmentNumber,
            @RequestParam String sessionToken) {
        attendanceService.markAttendanceByQR(enrollmentNumber, sessionToken);
        return ResponseEntity.ok(new ApiResponse<>("Attendance marked successfully via QR"));
    }

    // Get attendance by ID (ADMIN only)
    @GetMapping(path = "/{attendanceId}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<AttendanceDTO> getAttendanceById(@PathVariable Long attendanceId){
        return ResponseEntity.ok(attendanceService.getAttendanceById(attendanceId));
    }

    // Get all attendance by Session ID (ADMIN only)
    @GetMapping(path = "/session/{sessionId}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<List<AttendanceDTO>> getAllAttendanceBySessionId(@PathVariable Long sessionId) {
        return ResponseEntity.ok(attendanceService.getAllAttendanceBySessionId(sessionId));
    }

    // Get all attendance records (ADMIN only)
    @GetMapping(path = "/all")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<List<AttendanceDTO>> getAllAttendance() {
        return ResponseEntity.ok(attendanceService.getAllAttendance());
    }

    // Mark attendance manually (ADMIN only)
    @PostMapping(path = "/mark/manual")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<ApiResponse<String>> markAttendanceManually(
            @RequestParam Long studentId,
            @RequestParam Long sessionId) {
        attendanceService.markAttendanceManually(studentId, sessionId);
        return ResponseEntity.ok(new ApiResponse<>("Attendance marked manually"));
    }

    // Update attendance by ID(ADMIN only)
    @PutMapping(path = "/{attendanceId}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<AttendanceDTO> updateAttendanceById(@PathVariable Long attendanceId,
                                                              @RequestBody AttendanceDTO inputAttendance){
        return ResponseEntity.ok(attendanceService.updateAttendanceById(attendanceId, inputAttendance));
    }

    // Update attendance status(ADMIN only)
    @PutMapping(path = "/{attendanceId}/status/{status}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<AttendanceDTO> updateAttendanceById(@PathVariable Long attendanceId,
                                                              @PathVariable String status){
        return ResponseEntity.ok(attendanceService.updateAttendanceStatus(attendanceId,status));
    }

    // Delete attendance by ID(ADMIN only)
    @DeleteMapping(path = "/{attendanceId}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<ApiResponse<String>> deleteAttendanceById(@PathVariable Long attendanceId){
        attendanceService.deleteAttendanceById(attendanceId);
        return ResponseEntity.ok(new ApiResponse<>("Attendance deleted successfully."));
    }

    // Mark absent for remaining students(ADMIN only)
    @PostMapping(path = "/markForRemaining")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<ApiResponse<String>> markAbsentForRemainingStudents(@RequestParam Long sessionId) {
        attendanceService.markAbsentForRemainingStudents(sessionId);
        return ResponseEntity.ok(new ApiResponse<>("Attendance marked for remaining students for this session."));
    }

}
