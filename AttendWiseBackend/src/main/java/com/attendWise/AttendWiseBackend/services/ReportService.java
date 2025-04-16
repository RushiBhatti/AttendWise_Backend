package com.attendWise.AttendWiseBackend.services;

import com.attendWise.AttendWiseBackend.dto.ReportDTO;

import java.util.List;

public interface ReportService {

    ReportDTO getReportById(Long id);

    List<ReportDTO> getAllReports();

    List<ReportDTO> getAllReportsByUserId(Long userId);

    byte[] generateStudentReportById(Long studentId);

    byte[] generateSessionReport(Long sessionId);

    byte[] generateStudentReportByEnrollmentNumber(String enrollmentNumber);
}
