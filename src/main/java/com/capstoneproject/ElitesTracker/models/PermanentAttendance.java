package com.capstoneproject.ElitesTracker.models;

import com.capstoneproject.ElitesTracker.enums.AttendanceStatus;
import jakarta.persistence.*;
import lombok.*;

import static com.capstoneproject.ElitesTracker.utils.HardCoded.PERMANENT_ATTENDANCE;

@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Entity(name = PERMANENT_ATTENDANCE)
public class PermanentAttendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @Enumerated(EnumType.STRING)
    private AttendanceStatus status;
    private String date;
    private String dayOfWeek;
    private String ipAddress;
}
