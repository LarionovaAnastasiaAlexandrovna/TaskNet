package edu.domain.repository;

import edu.domain.entity.Projects;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectsRepository extends JpaRepository<Projects, Long> {
}
