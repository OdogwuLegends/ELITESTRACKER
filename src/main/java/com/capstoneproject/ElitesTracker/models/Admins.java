package com.capstoneproject.ElitesTracker.models;

import jakarta.persistence.*;
import lombok.*;

import static com.capstoneproject.ElitesTracker.utils.AppUtil.getCurrentTimeStampUsingZonedDateTime;
import static com.capstoneproject.ElitesTracker.utils.HardCoded.ADMINS;

@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Entity(name = ADMINS)
public class Admins {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String semicolonEmail;

    private String createdAt;

    @PrePersist
    public void setCreatedAt(){
        this.createdAt = getCurrentTimeStampUsingZonedDateTime();
    }
}
