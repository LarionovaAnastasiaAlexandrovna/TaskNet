package edu.repository;

import edu.entity.Task;
import edu.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TasksRepository extends JpaRepository<Task, Long> {
    List<Task> findAllByAssignedTo(User user);

    @Query(value = """
    SELECT t.* FROM Tasks t, Users U
    WHERE u.email = :email
    AND t.assignedTo = u.userId
    ORDER BY date_last_vue;
    """, nativeQuery = true)
    List<Task> findAllByUserEmailOrderByDateLastView(String email);

    @Modifying
    @Transactional
    @Query(value = "UPDATE tasks SET last_viewed_at = NOW() WHERE task_id = :id", nativeQuery = true)
    void updateLastViewById(@Param("id") Long id);
}
