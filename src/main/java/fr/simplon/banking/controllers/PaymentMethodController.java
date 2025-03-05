package fr.simplon.banking.controllers;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import fr.simplon.banking.models.*;
import fr.simplon.banking.dto.PaymentMethodDTO;
import fr.simplon.banking.repositories.*;
import java.util.List;

@RestController
@RequestMapping("/api/payment-methods")
public class PaymentMethodController {
    private final PaymentMethodRepository paymentMethodRepository;
    private final UserRepository userRepository;

    public PaymentMethodController(PaymentMethodRepository paymentMethodRepository, UserRepository userRepository) {
        this.paymentMethodRepository = paymentMethodRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<PaymentMethod> getUserPaymentMethods(Authentication authentication) {
        User currentUser = userRepository.findByUsername(authentication.getName()).orElseThrow();
        return paymentMethodRepository.findByOwner(currentUser);
    }

    @PostMapping
    public PaymentMethod createPaymentMethod(@RequestBody PaymentMethodDTO paymentMethodDTO, Authentication authentication) {
        User currentUser = userRepository.findByUsername(authentication.getName()).orElseThrow();
        
        PaymentMethod paymentMethod = PaymentMethod.builder()
            .name(paymentMethodDTO.getName())
            .lastDigits(paymentMethodDTO.getLastDigits())
            .owner(currentUser)
            .build();

        return paymentMethodRepository.save(paymentMethod);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePaymentMethod(@PathVariable Long id, Authentication authentication) {
        User currentUser = userRepository.findByUsername(authentication.getName()).orElseThrow();
        PaymentMethod paymentMethod = paymentMethodRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Payment method not found"));
            
        if (!paymentMethod.getOwner().getId().equals(currentUser.getId())) {
            return ResponseEntity.status(403).body("Not authorized to delete this payment method");
        }
        
        paymentMethodRepository.delete(paymentMethod);
        return ResponseEntity.ok().build();
    }
}
