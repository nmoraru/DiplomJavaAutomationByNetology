package ru.netology.web.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardInfo {
    private String cardNumber;
    private String cardOwner;
    private String cardYear;
    private String cardMonth;
    private String code;
}
