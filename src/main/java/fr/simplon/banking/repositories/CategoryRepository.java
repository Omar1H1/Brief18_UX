package fr.simplon.banking.repositories;

import fr.simplon.banking.models.Category;
import fr.simplon.banking.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByIdAndOwner(Long id, User owner);
    List<Category> findByOwner(User owner);
}
