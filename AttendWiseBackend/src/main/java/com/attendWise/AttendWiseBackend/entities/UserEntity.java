package com.attendWise.AttendWiseBackend.entities;

import com.attendWise.AttendWiseBackend.entities.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long studentId;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String enrollmentNumber;

    @Column(nullable = false, length = 10)
    private String mobileNumber;

    @Column(nullable = false)
    private String branch;

    @Column(nullable = false)
    private Integer semester;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;  // STUDENT or ADMIN

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDate createdAt;

    @OneToMany(mappedBy = "studentId", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<AttendanceEntity> attendanceEntitySet;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<ReportEntity> reports;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity that = (UserEntity) o;
        return Objects.equals(getStudentId(), that.getStudentId()) && Objects.equals(getFirstName(), that.getFirstName()) && Objects.equals(getLastName(), that.getLastName()) && Objects.equals(getEmail(), that.getEmail()) && Objects.equals(getPassword(), that.getPassword()) && Objects.equals(getEnrollmentNumber(), that.getEnrollmentNumber()) && Objects.equals(getMobileNumber(), that.getMobileNumber()) && Objects.equals(getBranch(), that.getBranch()) && Objects.equals(getSemester(), that.getSemester()) && Objects.equals(getRole(), that.getRole()) && Objects.equals(getCreatedAt(), that.getCreatedAt());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStudentId(), getFirstName(), getLastName(), getEmail(), getPassword(), getEnrollmentNumber(), getMobileNumber(), getBranch(), getSemester(), getRole(), getCreatedAt());
    }
}