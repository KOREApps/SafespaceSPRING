package no.ntnu.kore.safespace.repository;

import no.ntnu.kore.safespace.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findUserByUsernameIgnoreCase(String username);

}
