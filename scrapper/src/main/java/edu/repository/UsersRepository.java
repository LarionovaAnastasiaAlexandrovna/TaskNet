package edu.repository;

import edu.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

//    @Query(value = """
//    SELECT u.* FROM Users u
//    JOIN ProjectUsers pu ON p.projectId = pu.idproject
//    JOIN Users u ON pu.iduser = u.userId
//    WHERE u.email = :email
//    """, nativeQuery = true)
//    List<User> findAllByUserEmail(String email);

//    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM Users u WHERE u.email = :email")
//    Boolean isExistUserByEmail(@Param("email") String email);

}
