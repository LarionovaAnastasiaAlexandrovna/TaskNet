package edu.entity;

import enums.ProjectStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "projectId", nullable = false)
    Long projectId;

    @Column(name = "projectname", nullable = false, length = 255)
    String projectName;

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    String description;

    @Column(name = "startdate", nullable = false)
    LocalDate startDate;

    @Column(name = "enddate", nullable = false)
    LocalDate endDate;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    ProjectStatus status;
}

