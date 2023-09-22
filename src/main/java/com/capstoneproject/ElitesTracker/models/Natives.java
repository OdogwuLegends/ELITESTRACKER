package com.capstoneproject.ElitesTracker.models;

import jakarta.persistence.*;
import lombok.*;

import static com.capstoneproject.ElitesTracker.utils.AppUtil.getCurrentTimeStamp;
import static com.capstoneproject.ElitesTracker.utils.HardCoded.NATIVES;

@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Entity(name = NATIVES)
public class Natives {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String semicolonEmail;

    @Column(nullable = false)
    private String cohort;

    @Column(nullable = false, unique = true)
    private String semicolonID;

    private String createdAt;

    @PrePersist
    public void setCreatedAt(){
        this.createdAt = getCurrentTimeStamp();
    }
}
