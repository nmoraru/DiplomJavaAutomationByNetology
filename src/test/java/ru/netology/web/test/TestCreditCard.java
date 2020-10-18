package ru.netology.web.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import lombok.val;
import org.junit.jupiter.api.*;
import org.testng.annotations.BeforeTest;
import ru.netology.web.page.CardPage;
import ru.netology.web.page.TitlePage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static ru.netology.web.data.DataGenerator.*;
import static ru.netology.web.data.SQLHelper.*;

public class TestCreditCard {
    TitlePage titlePage = open("http://localhost:8080", TitlePage.class);
    CardPage creditOrder = titlePage.creditCardPayment();

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

    @BeforeTest
    public void openCreditPaymentPage() {
        creditOrder.isVisibleCreditCard();
    }

    // Позитивные тест-кейсы

    @Test
    void shouldPaymentByValidCreditCard() {
        val validCreditCard = generateValidCard();
        creditOrder.PaymentByCard(validCreditCard);
        creditOrder.successfulPaymentMessage();
    }

    @Test
    void shouldWriteToDataBaseApprovedPaymentByCreditCard() throws InterruptedException {
        val validCreditCard = generateValidCard();
        creditOrder.PaymentByCard(validCreditCard);

        Thread.sleep(10000);
        assertEquals("APPROVED", searchOperationStatusByCreditCard());
        assertNotNull(searchOperationByOrderTableForCreditCard());
    }

    @Test
    void shouldPaymentByValidCreditCardWithMaxValidCardOwner() {
        val validCreditCard = generateValidCardWithMaxValidCardOwner();
        creditOrder.PaymentByCard(validCreditCard);
        creditOrder.successfulPaymentMessage();
    }

    @Test
    void shouldPaymentByValidCreditCardWithMinValidCardOwner() {
        val validCreditCard = generateValidCardWithMinValidCardOwner();
        creditOrder.PaymentByCard(validCreditCard);
        creditOrder.successfulPaymentMessage();
    }

    @Test
    void shouldPaymentByCardWithOwnerByDoubleSurname() {
        val creditCard = generateCardWithOwnerByDoubleSurname();
        creditOrder.PaymentByCard(creditCard);
        creditOrder.successfulPaymentMessage();
    }

    // Негативные тест-кейсы на поле "Номер карты"

    @Test
    void shouldDeclinedPaymentByCardWithExistIncorrectNumber() {
        val invalidCreditCard = generateExistInvalidCardNumberForDeclinedOperation();
        creditOrder.PaymentByCard(invalidCreditCard);

        creditOrder.errorPaymentMessage();
    }

    @Test
    void shouldWriteToDataBaseDeclinedPaymentByCreditCard() throws InterruptedException {
        val invalidCreditCard = generateExistInvalidCardNumberForDeclinedOperation();
        creditOrder.PaymentByCard(invalidCreditCard);

        Thread.sleep(10000);
        assertEquals("DECLINED", searchOperationStatusByCreditCard());
        assertNotNull(searchOperationByOrderTableForCreditCard());
    }

    @Test
    void shouldDeclinedPaymentByCardWithNotExistNumber() {
        val invalidCreditCard = generateNoExistCardNumberForDeclinedOperation();
        creditOrder.PaymentByCard(invalidCreditCard);

        creditOrder.errorPaymentMessage();
    }

    @Test
    void shouldDeclinedPaymentByCardWithEmptyNumber() {
        val invalidCreditCard = generateCardWithEmptyNumberField();
        creditOrder.PaymentByCard(invalidCreditCard);

        creditOrder.errorMessageByEmptyField();
    }

    @Test
    void shouldDeclinedPaymentByCardWithNoFullNumber() {
        val creditCard = generateCardWithNoFullNumber();
        creditOrder.PaymentByCard(creditCard);

        creditOrder.errorMessageByFieldIncorrectFormat();
    }

    @Test
    void shouldDeclinedPaymentByCardWithWordInNumber() {
        val creditCard = generateCardWithWordInNumber();
        creditOrder.PaymentByCard(creditCard);

        creditOrder.errorMessageByEmptyField();
    }

    @Test
    void shouldDeclinedPaymentByCardWithSpecialCharacterInNumber() {
        val creditCard = generateCardWithSpecialCharacterInNumber();
        creditOrder.PaymentByCard(creditCard);

        creditOrder.errorMessageByEmptyField();
    }

    @Test
    void shouldDeclinedPaymentByCardWithSpacesInNumber() {
        val creditCard = generateCardWithSpaceInNumber();
        creditOrder.PaymentByCard(creditCard);

        creditOrder.errorMessageByFieldIncorrectFormat();
    }

    // Негативные тест-кейсы на поле "Месяц"

    @Test
    void shouldDeclinedPaymentByCardWithEmptyMonth() {
        val invalidCreditCard = generateCardWithEmptyMonthField();
        creditOrder.PaymentByCard(invalidCreditCard);

        creditOrder.errorMessageByEmptyField();
    }

    @Test
    void shouldDeclinedPaymentByCardWithMonthUnderMin() {
        val invalidCreditCard = generateCardWithMonthUnderMin();
        creditOrder.PaymentByCard(invalidCreditCard);

        creditOrder.errorMessageByFieldIncorrectFormat();
    }

    @Test
    void shouldDeclinedPaymentByCardWithOneNumInMonth() {
        val invalidCreditCard = generateCardWithOneNumInMonth();
        creditOrder.PaymentByCard(invalidCreditCard);

        creditOrder.errorMessageByFieldIncorrectFormat();
    }

    @Test
    void shouldDeclinedPaymentByCardWithThreeNumInMonth() {
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
        val invalidCreditCard = generateCardWithMonthUpperMax();
        creditOrder.PaymentByCard(invalidCreditCard);

        creditOrder.errorMessageByIncorrectFormatMonthField();
    }

    @Test
    void shouldDeclinedPaymentByCardWithWordInMonth() {
        val creditCard = generateCardWithWordInNumber();
        creditOrder.PaymentByCard(creditCard);

        creditOrder.errorMessageByEmptyField();
    }

    @Test
    void shouldDeclinedPaymentByCardWithSpecialCharacterInMonth() {
        val creditCard = generateCardWithSpecialCharacterInMonth();
        creditOrder.PaymentByCard(creditCard);

        creditOrder.errorMessageByEmptyField();
    }

    @Test
    void shouldDeclinedPaymentByCardWithSpacesInMonth() {
        val creditCard = generateCardWithSpaceInMonth();
        creditOrder.PaymentByCard(creditCard);

        creditOrder.errorMessageByFieldIncorrectFormat();
    }

    @Test
    void shouldDeclinedPaymentByCardWithOldMonth() {
        val invalidCreditCard = generateCardWithOldMonth();
        creditOrder.PaymentByCard(invalidCreditCard);

        creditOrder.errorMessageByIncorrectFormatMonthField();
    }

    // Негативные тест-кейсы на поле "Год"

    @Test
    void shouldDeclinedPaymentByCardWithEmptyYear() {
        val invalidCreditCard = generateCardWithEmptyYearField();
        creditOrder.PaymentByCard(invalidCreditCard);
        creditOrder.errorMessageByEmptyField();
    }

    @Test
    void shouldDeclinedPaymentByCardWithOldYear() {
        val invalidCreditCard = generateCardWithOldYear();
        creditOrder.PaymentByCard(invalidCreditCard);
        creditOrder.errorMessageByOldYear();
    }

    @Test
    void shouldDeclinedPaymentByCardWithOneNumInYear() {
        val invalidCreditCard = generateCardWithOneNumInYear();
        creditOrder.PaymentByCard(invalidCreditCard);
        creditOrder.errorMessageByFieldIncorrectFormat();
    }

    @Test
    void shouldDeclinedPaymentByCardWithThreeNumInYear() {
        val invalidCreditCard = generateCardWithThreeNumInYear();
        creditOrder.PaymentByCard(invalidCreditCard);

        if (Integer.parseInt(invalidCreditCard.getCardYear()) / 10 >= 20) {
            creditOrder.successfulPaymentMessage();
        } else
            creditOrder.errorMessageByOldYear();
    }

    @Test
    void shouldDeclinedPaymentByCardWithWordInYear() {
        val creditCard = generateCardWithWordInYear();
        creditOrder.PaymentByCard(creditCard);
        creditOrder.errorMessageByEmptyField();
    }

    @Test
    void shouldDeclinedPaymentByCardWithSpecialCharacterInYear() {
        val creditCard = generateCardWithSpecialCharacterInYear();
        creditOrder.PaymentByCard(creditCard);
        creditOrder.errorMessageByEmptyField();
    }

    @Test
    void shouldDeclinedPaymentByCardWithSpacesInYear() {
        val creditCard = generateCardWithSpaceInYear();
        creditOrder.PaymentByCard(creditCard);
        creditOrder.errorMessageByFieldIncorrectFormat();
    }

    // Негативные тест-кейсы на поле "Владелец"

    @Test
    void shouldDeclinedPaymentByCardWithEmptyOwner() {
        val creditCard = generateCardWithEmptyOwnerField();
        creditOrder.PaymentByCard(creditCard);
        creditOrder.errorMessageByEmptyField();
    }

    @Test
    void shouldDeclinedPaymentByCardWithOwnerUpperMax() {
        val creditCard = generateCardWithOwnerUpperMax31Symbols();
        creditOrder.PaymentByCard(creditCard);
        creditOrder.errorMessageByFieldIncorrectFormat();
    }

    @Test
    void shouldDeclinedPaymentByCardWithOwnerByLowerCase() {
        val creditCard = generateCardWithOwnerByLowerCase();
        creditOrder.PaymentByCard(creditCard);
        creditOrder.errorMessageByFieldIncorrectFormat();
    }

    @Test
    void shouldDeclinedPaymentByCardWithOwnerByUpperCase() {
        val creditCard = generateCardWithOwnerByUpperCase();
        creditOrder.PaymentByCard(creditCard);
        creditOrder.errorMessageByFieldIncorrectFormat();
    }

    @Test
    void shouldDeclinedPaymentByCardWithOwnerByRusName() {
        val creditCard = generateCardWithFullName("ru");
        creditOrder.PaymentByCard(creditCard);
        creditOrder.errorMessageByFieldIncorrectFormat();
    }

    @Test
    void shouldDeclinedPaymentByCardWithOwnerCnName() {
        val creditCard = generateCardWithFullName("zh-CN");
        creditOrder.PaymentByCard(creditCard);
        creditOrder.errorMessageByFieldIncorrectFormat();
    }

    @Test
    void shouldDeclinedPaymentByCardWithOwnerArabicName() {
        val creditCard = generateCardWithFullName("ar");
        creditOrder.PaymentByCard(creditCard);
        creditOrder.errorMessageByFieldIncorrectFormat();
    }

    @Test
    void shouldDeclinedPaymentByCardWithNumericOwner() {
        val creditCard = generateCardWithNumericOwner();
        creditOrder.PaymentByCard(creditCard);
        creditOrder.errorMessageByFieldIncorrectFormat();
    }

    @Test
    void shouldDeclinedPaymentByCardWithOwnerBySpacesBeforeText() {
        val creditCard = generateCardWithOwnerBySpacesBeforeText();
        creditOrder.PaymentByCard(creditCard);
        creditOrder.errorMessageByFieldIncorrectFormat();
    }

    @Test
    void shouldDeclinedPaymentByCardWithOwnerBySpacesAfterText() {
        val creditCard = generateCardWithOwnerBySpacesAfterText();
        creditOrder.PaymentByCard(creditCard);
        creditOrder.errorMessageByFieldIncorrectFormat();
    }

    @Test
    void shouldDeclinedPaymentByCardWithOwnerByMoreSpacesBetweenNameAndSurname() {
        val creditCard = generateCardWithOwnerByMoreSpacesBetweenNameAndSurname();
        creditOrder.PaymentByCard(creditCard);
        creditOrder.errorMessageByFieldIncorrectFormat();
    }

    @Test
    void shouldDeclinedPaymentByCardWithOwnerByHyphenInStartName() {
        val creditCard = generateCardWithOwnerByHyphenInStartName();
        creditOrder.PaymentByCard(creditCard);
        creditOrder.errorMessageByFieldIncorrectFormat();
    }

    @Test
    void shouldDeclinedPaymentByCardWithOwnerByHyphenInFinishName() {
        val creditCard = generateCardWithOwnerByHyphenInFinishName();
        creditOrder.PaymentByCard(creditCard);
        creditOrder.errorMessageByFieldIncorrectFormat();
    }

    // Негативные тест-кейсы на поле "CVC/CVV"

    @Test
    void shouldDeclinedPaymentByCardWithEmptyCVC() {
        val invalidCreditCard = generateCardWithEmptyCVCField();
        creditOrder.PaymentByCard(invalidCreditCard);
        creditOrder.errorMessageByEmptyField();
    }

    @Test
    void shouldDeclinedPaymentByCardWithOneNumInCVC() {
        val invalidCreditCard = generateCardWithOneNumInCVC();
        creditOrder.PaymentByCard(invalidCreditCard);
        creditOrder.errorMessageByFieldIncorrectFormat();
    }

    @Test
    void shouldDeclinedPaymentByCardWithTwoNumInCVC() {
        val invalidCreditCard = generateCardWithTwoNumInCVC();
        creditOrder.PaymentByCard(invalidCreditCard);
        creditOrder.errorMessageByFieldIncorrectFormat();
    }

    @Test
    void shouldDeclinedPaymentByCardWithWordInCVC() {
        val creditCard = generateCardWithWordInCVC();
        creditOrder.PaymentByCard(creditCard);
        creditOrder.errorMessageByEmptyField();
    }

    @Test
    void shouldDeclinedPaymentByCardWithSpecialCharacterInCVC() {
        val creditCard = generateCardWithSpecialCharacterInCVC();
        creditOrder.PaymentByCard(creditCard);
        creditOrder.errorMessageByEmptyField();
    }

    @Test
    void shouldDeclinedPaymentByCardWithSpacesInCVC() {
        val creditCard = generateCardWithSpaceInCVC();
        creditOrder.PaymentByCard(creditCard);
        creditOrder.errorMessageByFieldIncorrectFormat();
    }
}
