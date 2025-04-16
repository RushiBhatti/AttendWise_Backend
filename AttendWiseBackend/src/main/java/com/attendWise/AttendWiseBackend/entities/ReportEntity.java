package com.attendWise.AttendWiseBackend.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "reports")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReportEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "student_id")
    private UserEntity student;

    @Column(name = "generated_at", nullable = false, updatable = false)
    private LocalDateTime generatedAt;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ReportEntity that = (ReportEntity) o;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getStudent(), that.getStudent()) && Objects.equals(getGeneratedAt(), that.getGeneratedAt());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getStudent(), getGeneratedAt());
    }
}