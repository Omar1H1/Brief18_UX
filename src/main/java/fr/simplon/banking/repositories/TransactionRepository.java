package fr.simplon.banking.repositories;

import fr.simplon.banking.models.Transaction;
import fr.simplon.banking.models.User;

import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByOwner(User owner);
    List<Transaction> findByOwnerAndDateAfter(User owner, LocalDateTime date);
}
