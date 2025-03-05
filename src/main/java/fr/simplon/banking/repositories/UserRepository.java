package fr.simplon.banking.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.simplon.banking.models.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

}
