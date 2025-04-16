package com.attendWise.AttendWiseBackend.dto;

import com.attendWise.AttendWiseBackend.entities.UserEntity;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReportDTO {
    private Long id;

    @NotNull(message = "student can not be null!")
    private UserEntity student;

    private LocalDateTime generatedAt;
}
