package pl.luypawlowski.chessbackend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.luypawlowski.chessbackend.entities.User;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<User, Long> {
    Optional<User> findByLogin(String login);

    Boolean existsByLogin(String login);
}
