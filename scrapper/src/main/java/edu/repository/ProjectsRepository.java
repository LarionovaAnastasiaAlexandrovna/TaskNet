package edu.repository;

import edu.entity.Project;
import edu.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectsRepository extends JpaRepository<Project, Long> {

    @Query(value = """
    SELECT p.* FROM Projects p
    JOIN ProjectUsers pu ON p.projectId = pu.idproject
    JOIN Users u ON pu.iduser = u.userId
    WHERE u.email = :email
    """, nativeQuery = true)
    List<Project> findAllByUserEmail(@Param("email") String email);

    @Query("SELECT pu.user FROM ProjectUser pu WHERE pu.project.id = :id")
    List<User> findAllByProjectId(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query(value = """
        INSERT INTO ProjectUsers (iduser, idproject)
        SELECT u.userid, :id
        FROM Users u
        WHERE u.email = :email
        """, nativeQuery = true)
    void addUserByEmail(@Param("email") String email, @Param("id") Long id);

}
