package ua.org.code.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.org.code.persistence.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("from User u left join fetch u.role where u.login=?1")
    Optional<User> findByLogin(String login);
    @Query("from User u left join fetch u.role where u.email=?1")
    Optional<User> findByEmail(String email);
    @Query("from User u left join fetch u.role")
    List<User> findAll();

    @Query("from User u left join fetch u.role where u.id=?1")
    Optional<User> findById(Long id);
}
