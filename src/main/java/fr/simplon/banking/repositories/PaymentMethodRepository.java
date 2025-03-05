package fr.simplon.banking.repositories;

import fr.simplon.banking.models.PaymentMethod;
import fr.simplon.banking.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {
    PaymentMethod findByIdAndOwner(Long id, User owner);
    List<PaymentMethod> findByOwner(User owner);
}
