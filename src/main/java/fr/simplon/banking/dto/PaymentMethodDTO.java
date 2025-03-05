package fr.simplon.banking.dto;

import lombok.Data;

@Data
public class PaymentMethodDTO {
    private String name;
    private String lastDigits;
}
