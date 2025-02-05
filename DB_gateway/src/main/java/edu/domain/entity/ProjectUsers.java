package edu.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Setter
@Getter
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
@Table(name = "ProjectUsers"/*, schema = "LAA"*/)
public class ProjectUsers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", nullable = false)
    Long id;

    @ManyToOne
    @MapsId("projectId")
    @JoinColumn(name = "projectId", nullable = false, foreignKey = @ForeignKey(name = "fk_project_users_project"))
    Projects project;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "userId", nullable = false, foreignKey = @ForeignKey(name = "fk_project_users_user"))
    Users user;
}
