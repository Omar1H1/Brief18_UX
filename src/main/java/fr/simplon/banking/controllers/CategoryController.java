package fr.simplon.banking.controllers;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import fr.simplon.banking.models.*;
import fr.simplon.banking.dto.CategoryDTO;
import fr.simplon.banking.repositories.*;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public CategoryController(CategoryRepository categoryRepository, UserRepository userRepository) {
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<Category> getUserCategories(Authentication authentication) {
        User currentUser = userRepository.findByUsername(authentication.getName()).orElseThrow();
        return categoryRepository.findByOwner(currentUser);
    }

    @PostMapping
    public Category createCategory(@RequestBody CategoryDTO categoryDTO, Authentication authentication) {
        User currentUser = userRepository.findByUsername(authentication.getName()).orElseThrow();
        
        Category category = Category.builder()
            .name(categoryDTO.getName())
            .color(categoryDTO.getColor())
            .limit(categoryDTO.getLimit())
            .owner(currentUser)
            .build();

        return categoryRepository.save(category);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id, Authentication authentication) {
        User currentUser = userRepository.findByUsername(authentication.getName()).orElseThrow();
        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Category not found"));
            
        if (!category.getOwner().getId().equals(currentUser.getId())) {
            return ResponseEntity.status(403).body("Not authorized to delete this category");
        }
        
        categoryRepository.delete(category);
        return ResponseEntity.ok().build();
    }
}
