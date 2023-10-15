package com.capstoneproject.ElitesTracker.models;

import com.capstoneproject.ElitesTracker.enums.AdminPrivileges;
import com.capstoneproject.ElitesTracker.enums.AttendancePermission;
import com.capstoneproject.ElitesTracker.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;
import java.util.TreeSet;

import static com.capstoneproject.ElitesTracker.utils.AppUtil.getCurrentTimeStampUsingZonedDateTime;
import static com.capstoneproject.ElitesTracker.utils.HardCoded.ELITE_USER;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
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

    private String resetPasswordToken;

    @Enumerated(value = EnumType.STRING)
    private Role role;

    @Column(nullable = false)
    private String screenWidth;

    @Column(nullable = false)
    private String screenHeight;

    private String semicolonID;
    private String createdAt;

    @Enumerated(value = EnumType.STRING)
    private AttendancePermission permission;

    private String attendancePermissionSetBy;
    @ElementCollection
    private Set<AdminPrivileges> adminPrivilegesList;

    @PrePersist
    public void setCreatedAt(){
        this.createdAt = getCurrentTimeStampUsingZonedDateTime();
    }

    public EliteUser(){
        this.adminPrivilegesList = new TreeSet<>();
    }

}
