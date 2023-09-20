package com.capstoneproject.ElitesTracker.models;

import com.capstoneproject.ElitesTracker.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import static com.capstoneproject.ElitesTracker.utils.AppUtil.getCurrentTimeStamp;
import static com.capstoneproject.ElitesTracker.utils.HardCoded.ELITE_USER;

@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Entity(name = ELITE_USER)
public class EliteUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    private String cohort;

    @Column(unique = true, nullable = false)
    private String semicolonEmail;

    @Column(nullable = false)
    private String password;

    @Enumerated(value = EnumType.STRING)
    private Role role;

    @Column(nullable = false)
    private String screenWidth;

    @Column(nullable = false)
    private String screenHeight;

    private String semicolonID;
    private String createdAt;
    private boolean isPermittedForAttendance;

    @PrePersist
    public void setCreatedAt(){
        this.createdAt = getCurrentTimeStamp();
    }

}
