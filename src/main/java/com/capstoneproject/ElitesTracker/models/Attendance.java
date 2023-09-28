package com.capstoneproject.ElitesTracker.models;

import com.capstoneproject.ElitesTracker.enums.AttendanceStatus;
import jakarta.persistence.*;
import lombok.*;

import static com.capstoneproject.ElitesTracker.utils.AppUtil.getCurrentDayOfWeek;
import static com.capstoneproject.ElitesTracker.utils.AppUtil.getCurrentTimeStampUsingLocalDateTime;
import static com.capstoneproject.ElitesTracker.utils.HardCoded.ATTENDANCE;
import static com.capstoneproject.ElitesTracker.utils.HardCoded.ELITE_USER_ID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Entity(name = ATTENDANCE)
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = ELITE_USER_ID)
    private EliteUser user;

    @Enumerated(EnumType.STRING)
    private AttendanceStatus status;
    private String date;
    private String dayOfWeek;

    private String cohort;

    private String ipAddress;

    @PrePersist
    public void beforePersist(){
        this.date = getCurrentTimeStampUsingLocalDateTime();
        this.dayOfWeek = getCurrentDayOfWeek();
    }
}
