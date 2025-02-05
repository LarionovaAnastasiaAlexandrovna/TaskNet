package edu.domain.entity;

import edu.domain.enums.ProjectStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Setter
@Getter
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
@Table(name = "Projects"/*, schema = "LAA"*/)
public class Projects {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "projectId", nullable = false)
    Long projectId;

    @Column(name = "projectName", nullable = false, length = 255)
    String projectName;

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    String description;

    @Column(name = "startDate", nullable = false)
    LocalDate startDate;

    @Column(name = "endDate", nullable = false)
    LocalDate endDate;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    ProjectStatus status;
}

