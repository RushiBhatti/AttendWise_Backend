package com.attendWise.AttendWiseBackend.dto;

import com.attendWise.AttendWiseBackend.entities.SessionEntity;
import com.attendWise.AttendWiseBackend.entities.UserEntity;
import com.attendWise.AttendWiseBackend.entities.enums.AttendanceStatus;
import com.attendWise.AttendWiseBackend.entities.enums.ModeOfMarkingAttendance;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceDTO {

    private Long id;

    @NotNull(message = "studentId can not be null!")
    private UserEntity studentId;

    @NotNull(message = "sessionId can not be blank!")
    private SessionEntity sessionId;

    @PastOrPresent
    private LocalDateTime timeStamp;

    @Pattern(regexp = "^(QR_SCAN|MANUAL)$", message = "Mode of marking for attendance can be either 'QR_SCAN' or " +
            "'MANUAL'")
    @NotNull(message = "modeOrMarkingAttendance can not be null!")
    private ModeOfMarkingAttendance modeOfMarkingAttendance;

    @Pattern(regexp = "^(PRESENT|ABSENT)$", message = "Status of an attendance can be 'PRESENT' or 'ABSENT' only!")
    @NotNull(message = "status can not be null!")
    private AttendanceStatus status;
}
