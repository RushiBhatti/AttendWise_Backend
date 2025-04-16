package com.attendWise.AttendWiseBackend;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@RequiredArgsConstructor
@SpringBootApplication
public class AttendWiseBackendApplication {

    public static void main(String[] args) {
		SpringApplication.run(AttendWiseBackendApplication.class, args);
	}
}
