package com.capstoneproject.ElitesTracker.models;

import jakarta.persistence.*;
import lombok.*;

import static com.capstoneproject.ElitesTracker.utils.AppUtil.getCurrentTimeStampUsingZonedDateTime;
import static com.capstoneproject.ElitesTracker.utils.HardCoded.TIME_ELIGIBILITY;

@Setter
@Getter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Entity(name = TIME_ELIGIBILITY)
public class TimeEligibility {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int startHour;
    private int startMinute;
    private int endHour;
    private int endMinute;
    private String setBy;
    private String createdAt;

    @PrePersist
    public void setCreatedAt(){
        this.createdAt = getCurrentTimeStampUsingZonedDateTime();
    }
}
