package ru.netology.web.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import ru.netology.web.data.CardInfo;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class CardPage {
    private SelenideElement debitCardText = $(byText("Оплата по карте"));
    private SelenideElement creditCardText = $(byText("Кредит по данным карты"));
    private SelenideElement orderButton = $(byText("Продолжить"));
    private ElementsCollection fields = $$(".input__control");


    private SelenideElement numberField = $("[placeholder=\"0000 0000 0000 0000\"]");
    private SelenideElement ownerField = fields.get(3);
    private SelenideElement yearField = $("[placeholder='22']");
    private SelenideElement monthField = $("[placeholder='08']");
    private SelenideElement cvcField = $("[placeholder='999']");

    public void isVisibleDebitCard() {
        debitCardText.shouldBe(visible);
    }

    public void isVisibleCreditCard() {
        creditCardText.shouldBe(visible);
    }

    public void PaymentByCard(CardInfo card) {
        numberField.sendKeys(card.getCardNumber());
        ownerField.sendKeys(card.getCardOwner());
        yearField.sendKeys(card.getCardYear());
        monthField.sendKeys(card.getCardMonth());
        cvcField.sendKeys(card.getCode());
        orderButton.click();
    }

    public void successfulPaymentMessage() {
        $(byText("Операция одобрена Банком.")).waitUntil(Condition.visible, 15000);
    }

    public void errorPaymentMessage() {
        $(byText("Ошибка! Банк отказал в проведении операции.")).waitUntil(Condition.visible, 15000);
    }

    public void errorMessageByFieldIncorrectFormat() {
        $(byText("Неверный формат")).waitUntil(Condition.visible, 2000);
    }

    public void errorMessageByEmptyField() {
        $(byText("Поле обязательно для заполнения")).waitUntil(Condition.visible, 2000);
    }

    public void errorMessageByIncorrectFormatMonthField() {
        $(byText("Неверно указан срок действия карты")).waitUntil(Condition.visible, 2000);
    }

    public void errorMessageByOldYear() {
        $(byText("Истёк срок действия карты")).waitUntil(Condition.visible, 2000);
    }

}
