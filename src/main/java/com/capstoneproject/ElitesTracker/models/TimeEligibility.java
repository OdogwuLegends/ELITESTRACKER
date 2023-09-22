package com.capstoneproject.ElitesTracker.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Entity(name = "time_eligibility")
public class TimeEligibility {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int startHour;
    private int startMinute;
    private int endHour;
    private int endMinute;
}
