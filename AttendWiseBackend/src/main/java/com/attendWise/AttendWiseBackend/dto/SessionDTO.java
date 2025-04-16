package com.attendWise.AttendWiseBackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SessionDTO {
    private Long id;

    private String qrCodeData;

    private LocalDateTime createdAt;

    private LocalDateTime expiresAt;
}
