package fr.simplon.banking.dto;

import fr.simplon.banking.models.Transaction;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransactionResponseDTO {
    private Long id;
    private String title;
    private String description;
    private BigDecimal amount;
    private LocalDateTime date;
    private String categoryName;
    private String categoryColor;
    private String paymentMethodName;

    public static TransactionResponseDTO from(Transaction transaction) {
        TransactionResponseDTO dto = new TransactionResponseDTO();
        dto.setId(transaction.getId());
        dto.setTitle(transaction.getTitle());
        dto.setDescription(transaction.getDescription());
        dto.setAmount(transaction.getAmount());
        dto.setDate(transaction.getDate());
        dto.setCategoryName(transaction.getCategory().getName());
        dto.setCategoryColor(transaction.getCategory().getColor());
        dto.setPaymentMethodName(transaction.getPaymentMethod().getName());
        return dto;
    }
}
