package ru.netology.web.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class TitlePage {
    private SelenideElement debitCardButton = $(byText("Купить"));
    private SelenideElement creditCardButton = $(byText("Купить в кредит"));

    public CardPage debitCardPayment() {
        debitCardButton.click();
        return new CardPage();
    }

    public CardPage creditCardPayment() {
        creditCardButton.click();
        return new CardPage();
    }

}
