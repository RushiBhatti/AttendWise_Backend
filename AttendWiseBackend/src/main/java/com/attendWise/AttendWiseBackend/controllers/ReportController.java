package com.attendWise.AttendWiseBackend.controllers;

import com.attendWise.AttendWiseBackend.dto.ReportDTO;
import com.attendWise.AttendWiseBackend.dto.UserDTO;
import com.attendWise.AttendWiseBackend.services.ReportService;
import com.attendWise.AttendWiseBackend.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;
    private final UserService userService;

    // Get all reports by userId
    @GetMapping(path = "/all/student/{studentId}")
    public ResponseEntity<List<ReportDTO>> getAllReportsByStudentId(@PathVariable Long studentId){
        return ResponseEntity.ok(reportService.getAllReportsByUserId(studentId));
    }

    // Generate and download report for student by ID
    @GetMapping(path = "/studentId/{studentId}/download")
    public ResponseEntity<byte[]> downloadStudentReportById(@PathVariable Long studentId) {

        UserDTO userDTO = userService.getUserById(studentId);

        byte[] pdfBytes = reportService.generateStudentReportById(studentId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment",
                "Attendance_Report_Student_" + userDTO.getFirstName() + userDTO.getLastName() + ".pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(pdfBytes);
    }

    // Generate and download report for student by enrollment number
    @GetMapping(path = "/studentER/{enrollmentNumber}/download")
    public ResponseEntity<byte[]> downloadStudentReportByEnrollmentNumber(@PathVariable String enrollmentNumber) {

        UserDTO userDTO = userService.getUserByEnrollmentNumber(enrollmentNumber);

        byte[] pdfBytes = reportService.generateStudentReportByEnrollmentNumber(enrollmentNumber);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment",
                "Attendance_Report_Student_" + userDTO.getFirstName() + userDTO.getLastName() + ".pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(pdfBytes);
    }

    // Get report by ID (ADMIN ONLY)
    @GetMapping(path = "/{reportId}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<ReportDTO> getReportById(@PathVariable Long reportId){
        return ResponseEntity.ok(reportService.getReportById(reportId));
    }

    // Get all reports information (ADMIN ONLY)
    @GetMapping(path = "/all")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<List<ReportDTO>> getAllReports(){
        return ResponseEntity.ok(reportService.getAllReports());
    }

    // Generate and download report for session by ID (ADMIN ONLY)
    @GetMapping(path = "/session/{sessionId}/download")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<byte[]> downloadSessionReport(@PathVariable Long sessionId) {
        byte[] pdfBytes = reportService.generateSessionReport(sessionId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "Attendance_Report_Session_" + sessionId + ".pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(pdfBytes);
    }

}
