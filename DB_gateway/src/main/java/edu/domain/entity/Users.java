// TODO: разобраться что ему не нравится в Users. Возможно, надо будет добавить в класс ProjectUsers простой первичный ключ, а составной просто удалить
package edu.domain.entity;

import edu.domain.annotation.Email;
import edu.domain.enums.UserRole;
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
@Table(name = "Users"/*, schema = "LAA"*/)
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userId", nullable = false)
    Long userId;

    @Column(name = "userName", nullable = false, length = 255)
    String userName;

    @Email
    @Column(name = "email", nullable = false, unique = true, length = 255)
    String email;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "role", nullable = false, length = 50)
    UserRole role;

    @Column(name = "socialLinks", columnDefinition = "jsonb")
    String socialLinks;

    @Column(name = "birthDate")
    LocalDate birthDate;

    @Lob
    @Column(name = "profilePhoto")
    byte[] profilePhoto;

    @Column(name = "phoneNumber", length = 15)
    String phoneNumber;

    @Column(name = "location", length = 255)
    String location;

    @Column(name = "profileDescription", columnDefinition = "TEXT")
    String profileDescription;
}


