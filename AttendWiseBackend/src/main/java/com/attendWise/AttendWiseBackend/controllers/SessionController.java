package com.attendWise.AttendWiseBackend.controllers;

import com.attendWise.AttendWiseBackend.advices.ApiResponse;
import com.attendWise.AttendWiseBackend.dto.SessionDTO;
import com.attendWise.AttendWiseBackend.services.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/session")
@RequiredArgsConstructor
public class SessionController {

    private final SessionService sessionService;

    // Get Session by ID
    @GetMapping(path = "/{sessionId}")
    public ResponseEntity<SessionDTO> getSessionById(@PathVariable Long sessionId){
        return ResponseEntity.ok(sessionService.getSessionById(sessionId));
    }

    // Get QR Code image for session ID
    @GetMapping(path = "/qr/{sessionId}")
    public ResponseEntity<byte[]> getQRCodeImage(@PathVariable Long sessionId) {
        byte[] qrImage = sessionService.getQRCodeForSession(sessionId);
        return ResponseEntity
                .ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(qrImage);
    }

    // Validate/Check if session is active or not
    @GetMapping(path = "/validate/{sessionId}")
    public ResponseEntity<ApiResponse<String>> validateSession(@PathVariable Long sessionId) {
        boolean isActive = sessionService.isSessionActive(sessionId);
        return isActive
                ? ResponseEntity.ok(new ApiResponse<>("Session is active"))
                : ResponseEntity.status(HttpStatus.GONE).body(new ApiResponse<>("Session expired"));
    }

    // Get all sessions (ADMIN ONLY)
    @GetMapping(path = "/all")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<List<SessionDTO>> getAllSessions(){
        return ResponseEntity.ok(sessionService.getAllSessions());
    }

    // Generate a new session and QR code (ADMIN only)
    @PostMapping(path = "/generate")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<SessionDTO> generateQrCodeSession() {
        SessionDTO sessionDTO = sessionService.generateSessionForQRCode();
        return ResponseEntity.ok(sessionDTO);
    }

    // Delete session by ID (ADMIN ONLY)
    @DeleteMapping(path = "/{sessionId}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<ApiResponse<String>> deleteSessionById(@PathVariable Long sessionId){
        sessionService.deleteSession(sessionId);
        return ResponseEntity.ok(new ApiResponse<>("Session deleted successfully."));
    }

}
