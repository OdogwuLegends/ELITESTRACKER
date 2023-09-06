package com.capstoneproject.ElitesTracker.models;

import com.capstoneproject.ElitesTracker.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import static com.capstoneproject.ElitesTracker.utils.App.getCurrentTimeStamp;
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

    private String firstName;
    private String lastName;
    private String cohort;

    @Column(unique = true, nullable = false)
    private String semicolonEmail;

    @Column(nullable = false)
    private String semicolonWifiPassword;

    @Column(nullable = false)
    private String semicolonWifiUsername;

    @Enumerated(value = EnumType.STRING)
    private Role role;

    @Column(unique = true)
    private String IpAddress;
    private String createdAt;

    @PrePersist
    public void setCreatedAt(){
        this.createdAt = getCurrentTimeStamp();
    }

}
