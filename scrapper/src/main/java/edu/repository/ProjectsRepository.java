package edu.repository;

import edu.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
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
}
