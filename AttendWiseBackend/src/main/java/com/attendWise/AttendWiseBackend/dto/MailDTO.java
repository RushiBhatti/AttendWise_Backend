package com.attendWise.AttendWiseBackend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MailDTO {

    @NotBlank(message = "to can not be blank!")
    private String to;

    @NotBlank(message = "subject can not be blank!")
    private String subject;

    @NotBlank(message = "message can not be blank!")
    private String message;
}
