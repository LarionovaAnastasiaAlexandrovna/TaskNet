package edu.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
@Table(name = "Comments"/*, schema = "LAA"*/)
public class Comments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "commentId", nullable = false)
    Long commentId;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    String content;

    @ManyToOne
    @JoinColumn(name = "authorId", nullable = false, foreignKey = @ForeignKey(name = "fk_comment_user"))
    Users author;

    @ManyToOne
    @JoinColumn(name = "taskId", nullable = false, foreignKey = @ForeignKey(name = "fk_comment_task"))
    Tasks task;

    @Column(name = "date", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    LocalDateTime date = LocalDateTime.now();
}
// TODO: продумать и написать DTO-шки
// TODO: написать пока простые контроллеры