package com.attendWise.AttendWiseBackend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "sessions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SessionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "qr_code_data", columnDefinition = "TEXT")
    private String qrCodeData;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    @OneToMany(mappedBy = "sessionId", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<AttendanceEntity> attendanceEntities;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        SessionEntity that = (SessionEntity) o;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getQrCodeData(), that.getQrCodeData()) && Objects.equals(getCreatedAt(), that.getCreatedAt()) && Objects.equals(getExpiresAt(), that.getExpiresAt());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getQrCodeData(), getCreatedAt(), getExpiresAt());
    }
}
