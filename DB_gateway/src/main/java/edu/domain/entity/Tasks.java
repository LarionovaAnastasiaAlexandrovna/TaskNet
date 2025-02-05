package edu.domain.entity;

import edu.domain.enums.TaskPriority;
import edu.domain.enums.TaskStatus;
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
@Table(name = "Tasks"/*, schema = "LAA"*/)
public class Tasks {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "taskId", nullable = false)
    Long taskId;

    @Column(name = "taskName", nullable = false, length = 255)
    String taskName;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    TaskStatus status;

    @Column(name = "startDate", nullable = false)
    LocalDate startDate;

    @Column(name = "endDate", nullable = false)
    LocalDate endDate;

    @ManyToOne
    @JoinColumn(name = "projectId", nullable = false, foreignKey = @ForeignKey(name = "fk_task_project"))
    Projects project;

    @Column(name = "category", nullable = false, length = 50)
    String category;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "priority", nullable = false, length = 20)
    TaskPriority priority;

    @Column(name = "description", columnDefinition = "TEXT")
    String description;

    @ManyToOne
    @JoinColumn(name = "assignedTo", foreignKey = @ForeignKey(name = "fk_task_user"))
    Users assignedTo;

    @Column(name = "dependencies", columnDefinition = "JSONB")
    String dependencies;
}
