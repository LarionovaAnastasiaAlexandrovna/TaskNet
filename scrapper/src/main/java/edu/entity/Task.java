package edu.entity;

import enums.TaskPriority;
import enums.TaskStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
@Table(name = "Tasks"/*, schema = "LAA"*/)
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id", nullable = false)
    Long taskId;

    @Column(name = "taskname", nullable = false, length = 255)
    String taskName;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    TaskStatus status;

    @Column(name = "startdate", nullable = false)
    LocalDate startDate;

    @Column(name = "enddate", nullable = false)
    LocalDate endDate;

    @ManyToOne
    @JoinColumn(name = "projectid", nullable = false, foreignKey = @ForeignKey(name = "fk_task_project"))
    Project project;

    @Column(name = "category", nullable = false, length = 50)
    String category;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "priority", nullable = false, length = 20)
    TaskPriority priority;

    @Column(name = "description", columnDefinition = "TEXT")
    String description;

    @ManyToOne
    @JoinColumn(name = "assignedto", foreignKey = @ForeignKey(name = "fk_task_user"))
    User assignedTo;

    @Column(name = "date_create", nullable = false, updatable = false)
    LocalDateTime dateCreate;

    @Column(name = "date_last_view")
    LocalDateTime dateLastView;

//    @Column(name = "dependencies", columnDefinition = "JSONB")
//    String dependencies;

    @PrePersist
    protected void onCreate() {
        this.dateCreate = LocalDateTime.now();
        this.dateLastView = LocalDateTime.now();
    }
}
