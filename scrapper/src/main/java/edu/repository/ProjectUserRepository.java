package edu.repository;

import edu.entity.ProjectUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectUserRepository extends JpaRepository<ProjectUser, Long> {

}