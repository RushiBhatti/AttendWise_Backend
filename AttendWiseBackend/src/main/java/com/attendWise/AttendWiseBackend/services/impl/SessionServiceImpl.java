package com.attendWise.AttendWiseBackend.services.impl;

import com.attendWise.AttendWiseBackend.dto.SessionDTO;
import com.attendWise.AttendWiseBackend.entities.SessionEntity;
import com.attendWise.AttendWiseBackend.exceptions.ResourceNotFoundException;
import com.attendWise.AttendWiseBackend.repositories.SessionRepository;
import com.attendWise.AttendWiseBackend.services.JwtService;
import com.attendWise.AttendWiseBackend.services.SessionService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {

    private final SessionRepository sessionRepository;
    private final JwtService jwtService;
    private final ModelMapper modelMapper;

    @Override
    public SessionDTO getSessionById(Long id) {
        SessionEntity sessionEntity = sessionRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Session not found with id: " + id)
        );

        return modelMapper.map(sessionEntity, SessionDTO.class);
    }

//    @Override
//    public SessionDTO createSession(SessionDTO sessionDto) {
//        SessionEntity sessionEntity = new SessionEntity();
//        sessionEntity.setQrCodeData(sessionDto.getQrCodeData());
//        sessionEntity.setCreatedAt(LocalDateTime.now());
//        sessionEntity.setExpiresAt(LocalDateTime.now().plusMinutes(30));
//
//        return modelMapper.map(sessionRepository.save(sessionEntity), SessionDTO.class);
//    }

    @Override
    public SessionDTO generateSessionForQRCode() {
        SessionEntity session = new SessionEntity();
        session.setCreatedAt(LocalDateTime.now());
        session.setExpiresAt(LocalDateTime.now().plusMinutes(30));

        // Save session to generate ID
        session = sessionRepository.save(session);

        // Encode sessionId as JWT
        String encodedToken = jwtService.generateTokenFromSessionId(session.getId());

        // Store token as QR code data
        session.setQrCodeData(encodedToken);
        sessionRepository.save(session);

        return modelMapper.map(session, SessionDTO.class);
    }

    @Override
    public byte[] getQRCodeForSession(Long sessionId) {
        SessionEntity sessionEntity = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Session not found"));

        String encodedData = sessionEntity.getQrCodeData();

        try {
            // Using ZXing to generate QR code
            BitMatrix matrix = new MultiFormatWriter()
                    .encode(encodedData, BarcodeFormat.QR_CODE, 300, 300);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(matrix, "PNG", outputStream);
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate QR Code, exception: ", e);
        }
    }

    @Override
    public List<SessionDTO> getAllSessions() {
        return sessionRepository
                .findAll()
                .stream()
                .map((sessionEntity -> modelMapper.map(sessionEntity, SessionDTO.class)))
                .collect(Collectors.toList());
    }

    @Override
    public boolean isSessionActive(Long sessionId) {
        SessionEntity sessionEntity = sessionRepository.findById(sessionId).orElseThrow(
                () -> new ResourceNotFoundException("Session not found with id: " + sessionId)
        );

        return sessionEntity.getExpiresAt().isAfter(LocalDateTime.now());
    }

    @Override
    public void deleteSession(Long sessionId) {
        isSessionExistsById(sessionId);
        sessionRepository.deleteById(sessionId);
    }

    private void isSessionExistsById(Long id){
        if(!sessionRepository.existsById(id)){
            throw new ResourceNotFoundException("Session not found with id: " + id);
        }
    }
}
