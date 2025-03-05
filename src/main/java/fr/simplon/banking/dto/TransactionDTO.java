package fr.simplon.banking.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Builder;
import lombok.Data;

import com.fasterxml.jackson.annotation.JsonFormat;

@Data
public class TransactionDTO {
    private String title;
    private String description;
    private BigDecimal amount;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    @JsonFormat(pattern = "hh:mm")
    private LocalTime time = LocalTime.MIN;
    
    private Long categoryId;
    private Long paymentMethodId;
}
