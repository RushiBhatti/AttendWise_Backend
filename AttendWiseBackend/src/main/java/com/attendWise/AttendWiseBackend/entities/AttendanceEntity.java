package com.attendWise.AttendWiseBackend.entities;

import com.attendWise.AttendWiseBackend.entities.enums.AttendanceStatus;
import com.attendWise.AttendWiseBackend.entities.enums.ModeOfMarkingAttendance;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "attendance")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "student_id")
    private UserEntity studentId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "session_id")
    private SessionEntity sessionId;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime timeStamp;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "mode_of_marking")
    private ModeOfMarkingAttendance modeOfMarkingAttendance; // QR_SCAN or MANUAL

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private AttendanceStatus status; // PRESENT or ABSENT

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AttendanceEntity that = (AttendanceEntity) o;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getStudentId(), that.getStudentId()) && Objects.equals(getSessionId(), that.getSessionId()) && Objects.equals(getTimeStamp(), that.getTimeStamp()) && getModeOfMarkingAttendance() == that.getModeOfMarkingAttendance() && getStatus() == that.getStatus();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getStudentId(), getSessionId(), getTimeStamp(), getModeOfMarkingAttendance(), getStatus());
    }
}
