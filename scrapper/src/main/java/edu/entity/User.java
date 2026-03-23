package edu.entity;

import annotation.Email;
import enums.UserRole;
import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Builder
@Setter
@Getter
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
@Table(name = "Users"/*, schema = "LAA"*/)
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Access(AccessType.PROPERTY)  // Явный доступ через геттер
    @Column(name = "user_id", nullable = false)
    Long userId;

    @Column(name = "username", nullable = false)
    String userName;

    @Email
    @Column(name = "email", nullable = false, unique = true)
    String email;

    @Column(name = "password_hash", nullable = false)
    String passwordHash;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "role", nullable = false, length = 50)
    UserRole role;

    @Column(name = "sociallinks")
    String socialLinks;

    @Column(name = "birthdate")
    LocalDate birthDate;

    //@Lob
    //@Type(org.hibernate.type.BinaryType)
    @Column(name = "profilephoto", columnDefinition = "BYTEA")
    private byte[] profilePhoto;

    @Column(name = "phonenumber", length = 15)
    String phoneNumber;

    @Column(name = "location")
    String location;

    @Column(name = "profiledescription", columnDefinition = "TEXT")
    String profileDescription;

    @Column(name = "enabled")
    private boolean enabled;
}


