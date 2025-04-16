package com.attendWise.AttendWiseBackend.controllers;

import com.attendWise.AttendWiseBackend.dto.MailDTO;
import com.attendWise.AttendWiseBackend.services.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    // Send mail to user (ADMIN ONLY)
    @PostMapping(path = "/sendMail")
    @Secured("ROLE_ADMIN")
    public void sendMailToUser(
            @RequestBody MailDTO mailDTO){
        notificationService.sendEmail(mailDTO.getTo(), mailDTO.getSubject(), mailDTO.getMessage());
    }
}
