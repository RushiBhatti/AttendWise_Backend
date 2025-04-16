package com.attendWise.AttendWiseBackend.services;

import com.attendWise.AttendWiseBackend.entities.AttendanceEntity;
import com.attendWise.AttendWiseBackend.entities.UserEntity;
import com.attendWise.AttendWiseBackend.entities.enums.AttendanceStatus;
import com.attendWise.AttendWiseBackend.repositories.AttendanceRepository;
import com.attendWise.AttendWiseBackend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final JavaMailSender mailSender;
    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;

    public void sendEmail(String to, String subject, String body) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(to);
            mailMessage.setSubject(subject);
            mailMessage.setText(body);
            mailSender.send(mailMessage);
        }catch (Exception e){
            throw new RuntimeException("Error while sending mail, error: " + e);
        }
    }

    // Cron job to check today's attendance and send emails to absent users.
    // This method runs every day at 5:00 P.M.
    @Scheduled(cron = "0 0 17 * * ?")
    public void notifyAbsentUsers() {

        List<AttendanceEntity> absentees = attendanceRepository
                .findByStatus(AttendanceStatus.ABSENT.name())
                .stream()
                .filter(attendanceEntity -> attendanceEntity.getTimeStamp().isAfter(LocalDateTime.now().minusDays(1)))
                .toList();

        for (AttendanceEntity attendance : absentees) {
            UserEntity user = attendance.getStudentId();
            String subject = "Attendance Alert - You were absent!";
            String message = "Dear " + user.getFirstName() + user.getLastName() + ",\n\n"
                    + "You were marked absent for a session today(dt: "+ LocalDate.now() +"). Please contact your " +
                    "class coordinator if this is a mistake.\n\n"
                    + "Regards," +
                    "\nAttendWise Team";

            sendEmail(user.getEmail(), subject, message);
        }
    }
}
