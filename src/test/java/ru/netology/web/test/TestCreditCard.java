package ru.netology.web.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import lombok.val;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.netology.web.page.TitlePage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static ru.netology.web.data.DataGenerator.*;

public class TestCreditCard {
    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @AfterEach
    public void cleanTables() {
        cleanData();
    }

    // Позитивные тест-кейсы

    @Test
    void shouldPaymentByValidCreditCard() {
        val titlePage = open("http://localhost:8080", TitlePage.class);
        val creditOrder = titlePage.creditCardPayment();
        creditOrder.isVisibleCreditCard();

        val validCreditCard = generateValidCard();
        creditOrder.PaymentByCard(validCreditCard);
        creditOrder.successfulPaymentMessage();
    }

    @Test
    void shouldWriteToDataBaseApprovedPaymentByCreditCard() throws InterruptedException {
        val titlePage = open("http://localhost:8080", TitlePage.class);
        val creditOrder = titlePage.creditCardPayment();
        creditOrder.isVisibleCreditCard();

        val validCreditCard = generateValidCard();
        creditOrder.PaymentByCard(validCreditCard);

        Thread.sleep(10000);
        assertEquals("APPROVED", searchOperationStatusByCreditCard());
        assertNotNull(searchOperationByOrderTableForCreditCard());
    }

    @Test
    void shouldPaymentByValidCreditCardWithMaxValidCardOwner() {
        val titlePage = open("http://localhost:8080", TitlePage.class);
        val creditOrder = titlePage.creditCardPayment();
        creditOrder.isVisibleCreditCard();

        val validCreditCard = generateValidCardWithMaxValidCardOwner();
        creditOrder.PaymentByCard(validCreditCard);
        creditOrder.successfulPaymentMessage();
    }

    @Test
    void shouldPaymentByValidCreditCardWithMinValidCardOwner() {
        val titlePage = open("http://localhost:8080", TitlePage.class);
        val creditOrder = titlePage.creditCardPayment();
        creditOrder.isVisibleCreditCard();

        val validCreditCard = generateValidCardWithMinValidCardOwner();
        creditOrder.PaymentByCard(validCreditCard);
        creditOrder.successfulPaymentMessage();
    }

    @Test
    void shouldPaymentByCardWithOwnerByDoubleSurname() {
        val titlePage = open("http://localhost:8080", TitlePage.class);
        val creditOrder = titlePage.creditCardPayment();
        creditOrder.isVisibleCreditCard();

        val creditCard = generateCardWithOwnerByDoubleSurname();
        creditOrder.PaymentByCard(creditCard);
        creditOrder.successfulPaymentMessage();
    }

    // Негативные тест-кейсы на поле "Номер карты"

    @Test
    void shouldDeclinedPaymentByCardWithExistIncorrectNumber() {
        val titlePage = open("http://localhost:8080", TitlePage.class);
        val creditOrder = titlePage.creditCardPayment();
        creditOrder.isVisibleCreditCard();

        val invalidCreditCard = generateExistInvalidCardNumberForDeclinedOperation();
        creditOrder.PaymentByCard(invalidCreditCard);

        creditOrder.errorPaymentMessage();
    }

    @Test
    void shouldWriteToDataBaseDeclinedPaymentByCreditCard() throws InterruptedException {
        val titlePage = open("http://localhost:8080", TitlePage.class);
        val creditOrder = titlePage.creditCardPayment();
        creditOrder.isVisibleCreditCard();

        val invalidCreditCard = generateExistInvalidCardNumberForDeclinedOperation();
        creditOrder.PaymentByCard(invalidCreditCard);

        Thread.sleep(10000);
        assertEquals("DECLINED", searchOperationStatusByCreditCard());
        assertNotNull(searchOperationByOrderTableForCreditCard());
    }

    @Test
    void shouldDeclinedPaymentByCardWithNotExistNumber() {
        val titlePage = open("http://localhost:8080", TitlePage.class);
        val creditOrder = titlePage.creditCardPayment();
        creditOrder.isVisibleCreditCard();

        val invalidCreditCard = generateNoExistCardNumberForDeclinedOperation();
        creditOrder.PaymentByCard(invalidCreditCard);

        creditOrder.errorPaymentMessage();
    }

    @Test
    void shouldDeclinedPaymentByCardWithEmptyNumber() {
        val titlePage = open("http://localhost:8080", TitlePage.class);
        val creditOrder = titlePage.creditCardPayment();
        creditOrder.isVisibleCreditCard();

        val invalidCreditCard = generateCardWithEmptyNumberField();
        creditOrder.PaymentByCard(invalidCreditCard);

        creditOrder.errorMessageByEmptyField();
    }

    @Test
    void shouldDeclinedPaymentByCardWithNoFullNumber() {
        val titlePage = open("http://localhost:8080", TitlePage.class);
        val creditOrder = titlePage.creditCardPayment();
        creditOrder.isVisibleCreditCard();

        val creditCard = generateCardWithNoFullNumber();
        creditOrder.PaymentByCard(creditCard);

        creditOrder.errorMessageByFieldIncorrectFormat();
    }

    @Test
    void shouldDeclinedPaymentByCardWithWordInNumber() {
        val titlePage = open("http://localhost:8080", TitlePage.class);
        val creditOrder = titlePage.creditCardPayment();
        creditOrder.isVisibleCreditCard();

        val creditCard = generateCardWithWordInNumber();
        creditOrder.PaymentByCard(creditCard);

        creditOrder.errorMessageByEmptyField();
    }

    @Test
    void shouldDeclinedPaymentByCardWithSpecialCharacterInNumber() {
        val titlePage = open("http://localhost:8080", TitlePage.class);
        val creditOrder = titlePage.creditCardPayment();
        creditOrder.isVisibleCreditCard();

        val creditCard = generateCardWithSpecialCharacterInNumber();
        creditOrder.PaymentByCard(creditCard);

        creditOrder.errorMessageByEmptyField();
    }

    @Test
    void shouldDeclinedPaymentByCardWithSpacesInNumber() {
        val titlePage = open("http://localhost:8080", TitlePage.class);
        val creditOrder = titlePage.creditCardPayment();
        creditOrder.isVisibleCreditCard();

        val creditCard = generateCardWithSpaceInNumber();
        creditOrder.PaymentByCard(creditCard);

        creditOrder.errorMessageByFieldIncorrectFormat();
    }

    // Негативные тест-кейсы на поле "Месяц"

    @Test
    void shouldDeclinedPaymentByCardWithEmptyMonth() {
        val titlePage = open("http://localhost:8080", TitlePage.class);
        val creditOrder = titlePage.creditCardPayment();
        creditOrder.isVisibleCreditCard();

        val invalidCreditCard = generateCardWithEmptyMonthField();
        creditOrder.PaymentByCard(invalidCreditCard);

        creditOrder.errorMessageByEmptyField();
    }

    @Test
    void shouldDeclinedPaymentByCardWithMonthUnderMin() {
        val titlePage = open("http://localhost:8080", TitlePage.class);
        val creditOrder = titlePage.creditCardPayment();
        creditOrder.isVisibleCreditCard();

        val invalidCreditCard = generateCardWithMonthUnderMin();
        creditOrder.PaymentByCard(invalidCreditCard);

        creditOrder.errorMessageByFieldIncorrectFormat();
    }

    @Test
    void shouldDeclinedPaymentByCardWithOneNumInMonth() {
        val titlePage = open("http://localhost:8080", TitlePage.class);
        val creditOrder = titlePage.creditCardPayment();
        creditOrder.isVisibleCreditCard();

        val invalidCreditCard = generateCardWithOneNumInMonth();
        creditOrder.PaymentByCard(invalidCreditCard);

        creditOrder.errorMessageByFieldIncorrectFormat();
    }

    @Test
    void shouldDeclinedPaymentByCardWithThreeNumInMonth() {
        val titlePage = open("http://localhost:8080", TitlePage.class);
        val creditOrder = titlePage.creditCardPayment();
        creditOrder.isVisibleCreditCard();

        val invalidCreditCard = generateCardWithThreeNumInMonth();
        creditOrder.PaymentByCard(invalidCreditCard);

        if (Integer.parseInt(invalidCreditCard.getCardMonth()) / 10 <= 12) {
            creditOrder.successfulPaymentMessage();
        } else {
            creditOrder.errorMessageByIncorrectFormatMonthField();
        }
    }

    @Test
    void shouldDeclinedPaymentByCardWithMonthUpperMax() {
        val titlePage = open("http://localhost:8080", TitlePage.class);
        val creditOrder = titlePage.creditCardPayment();
        creditOrder.isVisibleCreditCard();

        val invalidCreditCard = generateCardWithMonthUpperMax();
        creditOrder.PaymentByCard(invalidCreditCard);

        creditOrder.errorMessageByIncorrectFormatMonthField();
    }

    @Test
    void shouldDeclinedPaymentByCardWithWordInMonth() {
        val titlePage = open("http://localhost:8080", TitlePage.class);
        val creditOrder = titlePage.creditCardPayment();
        creditOrder.isVisibleCreditCard();

        val creditCard = generateCardWithWordInNumber();
        creditOrder.PaymentByCard(creditCard);

        creditOrder.errorMessageByEmptyField();
    }

    @Test
    void shouldDeclinedPaymentByCardWithSpecialCharacterInMonth() {
        val titlePage = open("http://localhost:8080", TitlePage.class);
        val creditOrder = titlePage.creditCardPayment();
        creditOrder.isVisibleCreditCard();

        val creditCard = generateCardWithSpecialCharacterInMonth();
        creditOrder.PaymentByCard(creditCard);

        creditOrder.errorMessageByEmptyField();
    }

    @Test
    void shouldDeclinedPaymentByCardWithSpacesInMonth() {
        val titlePage = open("http://localhost:8080", TitlePage.class);
        val creditOrder = titlePage.creditCardPayment();
        creditOrder.isVisibleCreditCard();

        val creditCard = generateCardWithSpaceInMonth();
        creditOrder.PaymentByCard(creditCard);

        creditOrder.errorMessageByFieldIncorrectFormat();
    }

    @Test
    void shouldDeclinedPaymentByCardWithOldMonth() {
        val titlePage = open("http://localhost:8080", TitlePage.class);
        val creditOrder = titlePage.creditCardPayment();
        creditOrder.isVisibleCreditCard();

        val invalidCreditCard = generateCardWithOldMonth();
        creditOrder.PaymentByCard(invalidCreditCard);

        creditOrder.errorMessageByIncorrectFormatMonthField();
    }

    // Негативные тест-кейсы на поле "Год"

    @Test
    void shouldDeclinedPaymentByCardWithEmptyYear() {
        val titlePage = open("http://localhost:8080", TitlePage.class);
        val creditOrder = titlePage.creditCardPayment();
        creditOrder.isVisibleCreditCard();

        val invalidCreditCard = generateCardWithEmptyYearField();
        creditOrder.PaymentByCard(invalidCreditCard);
        creditOrder.errorMessageByEmptyField();
    }

    @Test
    void shouldDeclinedPaymentByCardWithOldYear() {
        val titlePage = open("http://localhost:8080", TitlePage.class);
        val creditOrder = titlePage.creditCardPayment();
        creditOrder.isVisibleCreditCard();

        val invalidCreditCard = generateCardWithOldYear();
        creditOrder.PaymentByCard(invalidCreditCard);
        creditOrder.errorMessageByOldYear();
    }

    @Test
    void shouldDeclinedPaymentByCardWithOneNumInYear() {
        val titlePage = open("http://localhost:8080", TitlePage.class);
        val creditOrder = titlePage.creditCardPayment();
        creditOrder.isVisibleCreditCard();

        val invalidCreditCard = generateCardWithOneNumInYear();
        creditOrder.PaymentByCard(invalidCreditCard);
        creditOrder.errorMessageByFieldIncorrectFormat();
    }

    @Test
    void shouldDeclinedPaymentByCardWithThreeNumInYear() {
        val titlePage = open("http://localhost:8080", TitlePage.class);
        val creditOrder = titlePage.creditCardPayment();
        creditOrder.isVisibleCreditCard();

        val invalidCreditCard = generateCardWithThreeNumInYear();
        creditOrder.PaymentByCard(invalidCreditCard);

        if (Integer.parseInt(invalidCreditCard.getCardYear()) / 10 >= 20) {
            creditOrder.successfulPaymentMessage();
        } else
            creditOrder.errorMessageByOldYear();
    }

    @Test
    void shouldDeclinedPaymentByCardWithWordInYear() {
        val titlePage = open("http://localhost:8080", TitlePage.class);
        val creditOrder = titlePage.creditCardPayment();
        creditOrder.isVisibleCreditCard();

        val creditCard = generateCardWithWordInYear();
        creditOrder.PaymentByCard(creditCard);
        creditOrder.errorMessageByEmptyField();
    }

    @Test
    void shouldDeclinedPaymentByCardWithSpecialCharacterInYear() {
        val titlePage = open("http://localhost:8080", TitlePage.class);
        val creditOrder = titlePage.creditCardPayment();
        creditOrder.isVisibleCreditCard();

        val creditCard = generateCardWithSpecialCharacterInYear();
        creditOrder.PaymentByCard(creditCard);
        creditOrder.errorMessageByEmptyField();
    }

    @Test
    void shouldDeclinedPaymentByCardWithSpacesInYear() {
        val titlePage = open("http://localhost:8080", TitlePage.class);
        val creditOrder = titlePage.creditCardPayment();
        creditOrder.isVisibleCreditCard();

        val creditCard = generateCardWithSpaceInYear();
        creditOrder.PaymentByCard(creditCard);
        creditOrder.errorMessageByFieldIncorrectFormat();
    }

    // Негативные тест-кейсы на поле "Владелец"

    @Test
    void shouldDeclinedPaymentByCardWithEmptyOwner() {
        val titlePage = open("http://localhost:8080", TitlePage.class);
        val creditOrder = titlePage.creditCardPayment();
        creditOrder.isVisibleCreditCard();

        val creditCard = generateCardWithEmptyOwnerField();
        creditOrder.PaymentByCard(creditCard);
        creditOrder.errorMessageByEmptyField();
    }

    @Test
    void shouldDeclinedPaymentByCardWithOwnerUpperMax() {
        val titlePage = open("http://localhost:8080", TitlePage.class);
        val creditOrder = titlePage.creditCardPayment();
        creditOrder.isVisibleCreditCard();

        val creditCard = generateCardWithOwnerUpperMax31Symbols();
        creditOrder.PaymentByCard(creditCard);
        creditOrder.errorMessageByFieldIncorrectFormat();
    }

    @Test
    void shouldDeclinedPaymentByCardWithOwnerByLowerCase() {
        val titlePage = open("http://localhost:8080", TitlePage.class);
        val creditOrder = titlePage.creditCardPayment();
        creditOrder.isVisibleCreditCard();

        val creditCard = generateCardWithOwnerByLowerCase();
        creditOrder.PaymentByCard(creditCard);
        creditOrder.errorMessageByFieldIncorrectFormat();
    }

    @Test
    void shouldDeclinedPaymentByCardWithOwnerByUpperCase() {
        val titlePage = open("http://localhost:8080", TitlePage.class);
        val creditOrder = titlePage.creditCardPayment();
        creditOrder.isVisibleCreditCard();

        val creditCard = generateCardWithOwnerByUpperCase();
        creditOrder.PaymentByCard(creditCard);
        creditOrder.errorMessageByFieldIncorrectFormat();
    }

    @Test
    void shouldDeclinedPaymentByCardWithOwnerByRusName() {
        val titlePage = open("http://localhost:8080", TitlePage.class);
        val creditOrder = titlePage.creditCardPayment();
        creditOrder.isVisibleCreditCard();

        val creditCard = generateCardWithFullName("ru");
        creditOrder.PaymentByCard(creditCard);
        creditOrder.errorMessageByFieldIncorrectFormat();
    }

    @Test
    void shouldDeclinedPaymentByCardWithOwnerCnName() {
        val titlePage = open("http://localhost:8080", TitlePage.class);
        val creditOrder = titlePage.creditCardPayment();
        creditOrder.isVisibleCreditCard();

        val creditCard = generateCardWithFullName("zh-CN");
        creditOrder.PaymentByCard(creditCard);
        creditOrder.errorMessageByFieldIncorrectFormat();
    }

    @Test
    void shouldDeclinedPaymentByCardWithOwnerArabicName() {
        val titlePage = open("http://localhost:8080", TitlePage.class);
        val creditOrder = titlePage.creditCardPayment();
        creditOrder.isVisibleCreditCard();

        val creditCard = generateCardWithFullName("ar");
        creditOrder.PaymentByCard(creditCard);
        creditOrder.errorMessageByFieldIncorrectFormat();
    }

    @Test
    void shouldDeclinedPaymentByCardWithNumericOwner() {
        val titlePage = open("http://localhost:8080", TitlePage.class);
        val creditOrder = titlePage.creditCardPayment();
        creditOrder.isVisibleCreditCard();

        val creditCard = generateCardWithNumericOwner();
        creditOrder.PaymentByCard(creditCard);
        creditOrder.errorMessageByFieldIncorrectFormat();
    }

    @Test
    void shouldDeclinedPaymentByCardWithOwnerBySpacesBeforeText() {
        val titlePage = open("http://localhost:8080", TitlePage.class);
        val creditOrder = titlePage.creditCardPayment();
        creditOrder.isVisibleCreditCard();

        val creditCard = generateCardWithOwnerBySpacesBeforeText();
        creditOrder.PaymentByCard(creditCard);
        creditOrder.errorMessageByFieldIncorrectFormat();
    }

    @Test
    void shouldDeclinedPaymentByCardWithOwnerBySpacesAfterText() {
        val titlePage = open("http://localhost:8080", TitlePage.class);
        val creditOrder = titlePage.creditCardPayment();
        creditOrder.isVisibleCreditCard();

        val creditCard = generateCardWithOwnerBySpacesAfterText();
        creditOrder.PaymentByCard(creditCard);
        creditOrder.errorMessageByFieldIncorrectFormat();
    }

    @Test
    void shouldDeclinedPaymentByCardWithOwnerByMoreSpacesBetweenNameAndSurname() {
        val titlePage = open("http://localhost:8080", TitlePage.class);
        val creditOrder = titlePage.creditCardPayment();
        creditOrder.isVisibleCreditCard();

        val creditCard = generateCardWithOwnerByMoreSpacesBetweenNameAndSurname();
        creditOrder.PaymentByCard(creditCard);
        creditOrder.errorMessageByFieldIncorrectFormat();
    }

    @Test
    void shouldDeclinedPaymentByCardWithOwnerByHyphenInStartName() {
        val titlePage = open("http://localhost:8080", TitlePage.class);
        val creditOrder = titlePage.creditCardPayment();
        creditOrder.isVisibleCreditCard();

        val creditCard = generateCardWithOwnerByHyphenInStartName();
        creditOrder.PaymentByCard(creditCard);
        creditOrder.errorMessageByFieldIncorrectFormat();
    }

    @Test
    void shouldDeclinedPaymentByCardWithOwnerByHyphenInFinishName() {
        val titlePage = open("http://localhost:8080", TitlePage.class);
        val creditOrder = titlePage.creditCardPayment();
        creditOrder.isVisibleCreditCard();

        val creditCard = generateCardWithOwnerByHyphenInFinishName();
        creditOrder.PaymentByCard(creditCard);
        creditOrder.errorMessageByFieldIncorrectFormat();
    }

    // Негативные тест-кейсы на поле "CVC/CVV"

    @Test
    void shouldDeclinedPaymentByCardWithEmptyCVC() {
        val titlePage = open("http://localhost:8080", TitlePage.class);
        val creditOrder = titlePage.creditCardPayment();
        creditOrder.isVisibleCreditCard();

        val invalidCreditCard = generateCardWithEmptyCVCField();
        creditOrder.PaymentByCard(invalidCreditCard);
        creditOrder.errorMessageByEmptyField();
    }

    @Test
    void shouldDeclinedPaymentByCardWithOneNumInCVC() {
        val titlePage = open("http://localhost:8080", TitlePage.class);
        val creditOrder = titlePage.creditCardPayment();
        creditOrder.isVisibleCreditCard();

        val invalidCreditCard = generateCardWithOneNumInCVC();
        creditOrder.PaymentByCard(invalidCreditCard);
        creditOrder.errorMessageByFieldIncorrectFormat();
    }

    @Test
    void shouldDeclinedPaymentByCardWithTwoNumInCVC() {
        val titlePage = open("http://localhost:8080", TitlePage.class);
        val creditOrder = titlePage.creditCardPayment();
        creditOrder.isVisibleCreditCard();

        val invalidCreditCard = generateCardWithTwoNumInCVC();
        creditOrder.PaymentByCard(invalidCreditCard);
        creditOrder.errorMessageByFieldIncorrectFormat();
    }

    @Test
    void shouldDeclinedPaymentByCardWithWordInCVC() {
        val titlePage = open("http://localhost:8080", TitlePage.class);
        val creditOrder = titlePage.creditCardPayment();
        creditOrder.isVisibleCreditCard();

        val creditCard = generateCardWithWordInCVC();
        creditOrder.PaymentByCard(creditCard);
        creditOrder.errorMessageByEmptyField();
    }

    @Test
    void shouldDeclinedPaymentByCardWithSpecialCharacterInCVC() {
        val titlePage = open("http://localhost:8080", TitlePage.class);
        val creditOrder = titlePage.creditCardPayment();
        creditOrder.isVisibleCreditCard();

        val creditCard = generateCardWithSpecialCharacterInCVC();
        creditOrder.PaymentByCard(creditCard);
        creditOrder.errorMessageByEmptyField();
    }

    @Test
    void shouldDeclinedPaymentByCardWithSpacesInCVC() {
        val titlePage = open("http://localhost:8080", TitlePage.class);
        val creditOrder = titlePage.creditCardPayment();
        creditOrder.isVisibleCreditCard();

        val creditCard = generateCardWithSpaceInCVC();
        creditOrder.PaymentByCard(creditCard);
        creditOrder.errorMessageByFieldIncorrectFormat();
    }
}
