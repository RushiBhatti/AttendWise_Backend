package com.attendWise.AttendWiseBackend.services.impl;

import com.attendWise.AttendWiseBackend.dto.ReportDTO;
import com.attendWise.AttendWiseBackend.entities.AttendanceEntity;
import com.attendWise.AttendWiseBackend.entities.ReportEntity;
import com.attendWise.AttendWiseBackend.entities.SessionEntity;
import com.attendWise.AttendWiseBackend.entities.UserEntity;
import com.attendWise.AttendWiseBackend.exceptions.ResourceNotFoundException;
import com.attendWise.AttendWiseBackend.repositories.AttendanceRepository;
import com.attendWise.AttendWiseBackend.repositories.ReportRepository;
import com.attendWise.AttendWiseBackend.repositories.SessionRepository;
import com.attendWise.AttendWiseBackend.repositories.UserRepository;
import com.attendWise.AttendWiseBackend.services.ReportService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final AttendanceRepository attendanceRepository;
    private final SessionRepository sessionRepository;
    private final ModelMapper modelMapper;

    @Override
    public ReportDTO getReportById(Long id) {
        ReportEntity reportEntity = reportRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Report not found with id: " + id)
        );

        return modelMapper.map(reportEntity, ReportDTO.class);
    }

    @Override
    public List<ReportDTO> getAllReports() {
        return reportRepository
                .findAll()
                .stream()
                .map((reportEntity -> modelMapper.map(reportEntity, ReportDTO.class)))
                .collect(Collectors.toList());
    }

    @Override
    public List<ReportDTO> getAllReportsByUserId(Long userId){
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User not found with id: " + userId)
        );

        return reportRepository
                .findByStudent(userEntity)
                .stream()
                .map((reportEntity -> modelMapper.map(reportEntity, ReportDTO.class)))
                .collect(Collectors.toList());
    }

    @Override
    public byte[] generateStudentReportById(Long studentId) {

        UserEntity student = userRepository.findById(studentId).orElseThrow(
                () -> new ResourceNotFoundException("Student not found with id: " + studentId)
        );

        return generateReport(student);
    }

    @Override
    public byte[] generateStudentReportByEnrollmentNumber(String enrollmentNumber) {
        UserEntity student = userRepository.findByEnrollmentNumber(enrollmentNumber).orElseThrow(
                () -> new ResourceNotFoundException("Student not found with id: " + enrollmentNumber)
        );
            return generateReport(student);
    }

    @Override
    public byte[] generateSessionReport(Long sessionId) {
        try {
            SessionEntity session = sessionRepository.findById(sessionId).orElseThrow(
                    () -> new ResourceNotFoundException("Session not found with id: " + sessionId)
            );

            List<AttendanceEntity> attendanceList = attendanceRepository.findBySessionId(session);

            Document document = new Document();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, out);

            document.open();
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);

            document.add(new Paragraph("Attendance Report for Session ID: " + sessionId, titleFont));
            document.add(new Paragraph("Created At: " + session.getCreatedAt()));
            document.add(new Paragraph("Expires At: " + session.getExpiresAt()));
            document.add(new Paragraph(" "));

            // Add table
            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setWidths(new int[]{3, 3, 2, 2});

            // Table header
            Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
            PdfPCell hcell;

            hcell = new PdfPCell(new Phrase("Student Name", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell);

            hcell = new PdfPCell(new Phrase("Enrollment Number", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell);

            hcell = new PdfPCell(new Phrase("Status", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell);

            hcell = new PdfPCell(new Phrase("Mode", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell);

            for (AttendanceEntity attendance : attendanceList) {
                UserEntity student = attendance.getStudentId();
                table.addCell(student.getFirstName() + " " + student.getLastName());
                table.addCell(student.getEnrollmentNumber());
                table.addCell(attendance.getStatus().name());
                table.addCell(attendance.getModeOfMarkingAttendance().name());
            }

            document.add(table);
            document.close();
            return out.toByteArray();
        }catch (Exception e){
            throw new RuntimeException("Error while generating report for a session");
        }
    }

    private byte[] generateReport(UserEntity student){
        try{

            // Adding entry into database
            reportRepository.save(new ReportEntity(null, student, LocalDateTime.now()));

            List<AttendanceEntity> attendanceList = attendanceRepository.findByStudentId(student);

            Document document = new Document();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, out);

            document.open();
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);

            document.add(new Paragraph("Attendance Report for " + student.getFirstName() + " " + student.getLastName(),
                    titleFont));
            document.add(new Paragraph("Enrollment: " + student.getEnrollmentNumber()));
            document.add(new Paragraph("Semester: " + student.getSemester() + ", Branch: " + student.getBranch()));
            document.add(new Paragraph(" "));

            // Add table
            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setWidths(new int[]{3, 3, 2, 2});

            // Table header
            Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
            PdfPCell hcell;

            hcell = new PdfPCell(new Phrase("Date", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell);

            hcell = new PdfPCell(new Phrase("Session ID", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell);

            hcell = new PdfPCell(new Phrase("Status", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell);

            hcell = new PdfPCell(new Phrase("Mode", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell);

            for (AttendanceEntity attendance : attendanceList) {
                table.addCell(attendance.getTimeStamp().toString());
                table.addCell(attendance.getSessionId().getId().toString());
                table.addCell(attendance.getStatus().name());
                table.addCell(attendance.getModeOfMarkingAttendance().name());
            }

            document.add(table);
            document.close();
            return out.toByteArray();
        }catch (Exception e){
            throw new RuntimeException("Error while generating report for a student");
        }
    }
}
