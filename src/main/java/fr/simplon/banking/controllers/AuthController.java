package fr.simplon.banking.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import fr.simplon.banking.dto.JwtAuthenticationResponse;
import fr.simplon.banking.dto.LoginRequest;
import fr.simplon.banking.models.Category;
import fr.simplon.banking.models.User;
import fr.simplon.banking.repositories.CategoryRepository;
import fr.simplon.banking.repositories.UserRepository;
import fr.simplon.banking.security.JwtTokenProvider;
import jakarta.persistence.EntityExistsException;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final CategoryRepository categoryRepository;
    
    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtTokenProvider tokenProvider, CategoryRepository categoryRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.categoryRepository = categoryRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<JwtAuthenticationResponse> authenticateUser(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(),
                loginRequest.getPassword()
            )
        );
        
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow();
        String jwt = tokenProvider.generateToken(authentication.getName());
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt, user.getId(), user.getUsername()));
    }
    
    @PostMapping("/register")
    public ResponseEntity<JwtAuthenticationResponse> registerUser(@RequestBody LoginRequest loginRequest) {
        boolean exist = userRepository.findByUsername(loginRequest.getUsername()).isPresent();
        
        if(exist) {
            throw new EntityExistsException("Account with this username already exist in database.");
        }

        User user = userRepository.saveAndFlush(User.builder()
            .username(loginRequest.getUsername())
            .password(passwordEncoder.encode(loginRequest.getPassword()))
            .build()
        );
        
        // Create default categories
        List<Category> defaultCategories = Arrays.asList(
            Category.builder().name("Food").color("#FF5733").owner(user).build(),
            Category.builder().name("Transport").color("#33FF57").owner(user).build(),
            Category.builder().name("Bills").color("#3357FF").owner(user).build(),
            Category.builder().name("Entertainment").color("#FF33F5").owner(user).build(),
            Category.builder().name("Shopping").color("#33FFF5").owner(user).build()
        );
        categoryRepository.saveAll(defaultCategories);
        
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(),
                loginRequest.getPassword()
            )
        );
        
        String jwt = tokenProvider.generateToken(authentication.getName());
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt, user.getId(), user.getUsername()));
    }
    
}
