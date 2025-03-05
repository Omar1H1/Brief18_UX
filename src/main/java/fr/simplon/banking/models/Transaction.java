package fr.simplon.banking.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    @Nullable
    private String description;
    private BigDecimal amount;
    private LocalDateTime date;

    @ManyToOne
    private User owner;

    @ManyToOne
    private Category category;

    @ManyToOne
    private PaymentMethod paymentMethod;
}
