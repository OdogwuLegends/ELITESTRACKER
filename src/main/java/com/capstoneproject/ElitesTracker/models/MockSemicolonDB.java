package com.capstoneproject.ElitesTracker.models;

import jakarta.persistence.*;
import lombok.*;

import static com.capstoneproject.ElitesTracker.utils.HardCoded.MOCK_SEMICOLON_DB;

@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Entity(name = MOCK_SEMICOLON_DB)
public class MockSemicolonDB {
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
}
