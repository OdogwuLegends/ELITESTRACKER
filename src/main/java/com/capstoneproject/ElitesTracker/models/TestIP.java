package com.capstoneproject.ElitesTracker.models;

import jakarta.persistence.*;
import lombok.*;

import static com.capstoneproject.ElitesTracker.utils.HardCoded.TEST_IP;

@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Entity(name = TEST_IP)
public class TestIP {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String firstIpAddress;
    private String secondIpAddress;
    private String thirdIpAddress;
    private String firstName;
    private String lastName;
}
