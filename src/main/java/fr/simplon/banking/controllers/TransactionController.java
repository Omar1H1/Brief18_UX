package fr.simplon.banking.controllers;

import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import fr.simplon.banking.models.*;
import fr.simplon.banking.dto.TransactionDTO;
import fr.simplon.banking.dto.TransactionResponseDTO;
import fr.simplon.banking.repositories.*;
import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final PaymentMethodRepository paymentMethodRepository;

    public TransactionController(TransactionRepository transactionRepository, 
                               UserRepository userRepository,
                               CategoryRepository categoryRepository,
                               PaymentMethodRepository paymentMethodRepository) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.paymentMethodRepository = paymentMethodRepository;
    }

    @GetMapping
    public List<TransactionResponseDTO> getUserTransactions(Authentication authentication) {
        User currentUser = userRepository.findByUsername(authentication.getName()).orElseThrow();
        return transactionRepository.findByOwner(currentUser)
            .stream()
            .map(TransactionResponseDTO::from)
            .toList();
    }

    @GetMapping("/statistics/monthly")
    public Map<String, BigDecimal> getMonthlyStatistics(
            Authentication authentication,
            @RequestParam(required = false) LocalDateTime date) {
        User currentUser = userRepository.findByUsername(authentication.getName()).orElseThrow();
        LocalDateTime startDate = date != null ? date : LocalDateTime.now();
        LocalDateTime oneMonthAgo = startDate.minus(30, ChronoUnit.DAYS);
        
        List<Transaction> transactions = transactionRepository.findByOwnerAndDateAfter(currentUser, oneMonthAgo);
        
        return transactions.stream()
            .collect(Collectors.groupingBy(
                t -> t.getCategory().getName(),
                Collectors.reducing(BigDecimal.ZERO, Transaction::getAmount, BigDecimal::add)
            ));
    }

    @PostMapping
    public Transaction createTransaction(@RequestBody TransactionDTO transactionDTO, Authentication authentication) {
        User currentUser = userRepository.findByUsername(authentication.getName()).orElseThrow();
        Category category = categoryRepository.findByIdAndOwner(transactionDTO.getCategoryId(), currentUser);
        PaymentMethod paymentMethod = paymentMethodRepository.findByIdAndOwner(transactionDTO.getPaymentMethodId(), currentUser);
        
        Transaction transaction = Transaction.builder()
            .title(transactionDTO.getTitle())
            .description(transactionDTO.getDescription())
            .amount(transactionDTO.getAmount())
            .date(LocalDateTime.of(transactionDTO.getDate(), transactionDTO.getTime()))
            .category(category)
            .paymentMethod(paymentMethod)
            .owner(currentUser)
            .build();

        return transactionRepository.save(transaction);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTransaction(@PathVariable Long id, Authentication authentication) {
        User currentUser = userRepository.findByUsername(authentication.getName()).orElseThrow();
        Transaction transaction = transactionRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Transaction not found"));
            
        if (!transaction.getOwner().getId().equals(currentUser.getId())) {
            return ResponseEntity.status(403).body("Not authorized to delete this transaction");
        }
        
        transactionRepository.delete(transaction);
        return ResponseEntity.ok().build();
    }
}
