package com.attendWise.AttendWiseBackend.services;

import com.attendWise.AttendWiseBackend.dto.SessionDTO;

import java.util.List;

public interface SessionService {

    SessionDTO getSessionById(Long id);

//    SessionDTO createSession(SessionDTO sessionDto);

    SessionDTO generateSessionForQRCode();

    byte[] getQRCodeForSession(Long sessionId);

    List<SessionDTO> getAllSessions();

    boolean isSessionActive(Long sessionId);

    void deleteSession(Long sessionId);
}
