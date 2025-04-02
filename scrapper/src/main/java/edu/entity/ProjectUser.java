package edu.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
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
public class ProjectUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", nullable = false)
    Long id;

    @ManyToOne
    @MapsId("projectId")
    @JoinColumn(name = "projectId", nullable = false, foreignKey = @ForeignKey(name = "fk_project_users_project"))
    Project project;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "userId", nullable = false, foreignKey = @ForeignKey(name = "fk_project_users_user"))
    User user;
}
