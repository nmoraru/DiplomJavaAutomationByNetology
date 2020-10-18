package ru.netology.web.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import lombok.val;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testng.annotations.BeforeTest;
import ru.netology.web.page.CardPage;
import ru.netology.web.page.TitlePage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static ru.netology.web.data.DataGenerator.*;
import static ru.netology.web.data.SQLHelper.*;

public class TestDebitCard {
    TitlePage titlePage = open("http://localhost:8080", TitlePage.class);
    CardPage debitOrder = titlePage.debitCardPayment();

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
        debitOrder.isVisibleCreditCard();
    }

    // Позитивные тест-кейсы

    @Test
    void shouldPaymentByValidDebitCard() {
        val validDebitCard = generateValidCard();
        debitOrder.PaymentByCard(validDebitCard);

        debitOrder.successfulPaymentMessage();
    }

    @Test
    void shouldWriteToDataBaseApprovedPaymentByDebitCard() throws InterruptedException {
        val validDebitCard = generateValidCard();
        debitOrder.PaymentByCard(validDebitCard);

        Thread.sleep(10000);
        assertEquals("APPROVED", searchOperationStatusByDebitCard());
        assertNotNull(searchOperationByOrderTableForDebitCard());
    }

    @Test
    void shouldPaymentByValidDebitCardWithMaxValidCardOwner() {
        val validDebitCard = generateValidCardWithMaxValidCardOwner();
        debitOrder.PaymentByCard(validDebitCard);

        debitOrder.successfulPaymentMessage();
    }

    @Test
    void shouldPaymentByValidDebitCardWithMinValidCardOwner() {
        val validDebitCard = generateValidCardWithMinValidCardOwner();
        debitOrder.PaymentByCard(validDebitCard);

        debitOrder.successfulPaymentMessage();
    }

    @Test
    void shouldPaymentByCardWithOwnerByDoubleSurname() {
        val debitCard = generateCardWithOwnerByDoubleSurname();
        debitOrder.PaymentByCard(debitCard);

        debitOrder.successfulPaymentMessage();
    }

    // Негативные тест-кейсы на поле "Номер карты"

    @Test
    void shouldDeclinedPaymentByCardWithExistIncorrectNumber() {
        val invalidDebitCard = generateExistInvalidCardNumberForDeclinedOperation();
        debitOrder.PaymentByCard(invalidDebitCard);

        debitOrder.errorPaymentMessage();
    }

    @Test
    void shouldWriteToDataBaseDeclinedPaymentByDebitCard() throws InterruptedException {
        val invalidDebitCard = generateExistInvalidCardNumberForDeclinedOperation();
        debitOrder.PaymentByCard(invalidDebitCard);

        Thread.sleep(10000);
        assertEquals("DECLINED", searchOperationStatusByDebitCard());
        assertNotNull(searchOperationByOrderTableForDebitCard());
    }

    @Test
    void shouldDeclinedPaymentByCardWithNotExistNumber() {
        val invalidDebitCard = generateNoExistCardNumberForDeclinedOperation();
        debitOrder.PaymentByCard(invalidDebitCard);

        debitOrder.errorPaymentMessage();
    }

    @Test
    void shouldDeclinedPaymentByCardWithEmptyNumber() {
        val invalidDebitCard = generateCardWithEmptyNumberField();
        debitOrder.PaymentByCard(invalidDebitCard);

        debitOrder.errorMessageByEmptyField();
    }

    @Test
    void shouldDeclinedPaymentByCardWithNoFullNumber() {
        val debitCard = generateCardWithNoFullNumber();
        debitOrder.PaymentByCard(debitCard);

        debitOrder.errorMessageByFieldIncorrectFormat();
    }

    @Test
    void shouldDeclinedPaymentByCardWithWordInNumber() {
        val debitCard = generateCardWithWordInNumber();
        debitOrder.PaymentByCard(debitCard);

        debitOrder.errorMessageByEmptyField();
    }

    @Test
    void shouldDeclinedPaymentByCardWithSpecialCharacterInNumber() {
        val debitCard = generateCardWithSpecialCharacterInNumber();
        debitOrder.PaymentByCard(debitCard);

        debitOrder.errorMessageByEmptyField();
    }

    @Test
    void shouldDeclinedPaymentByCardWithSpacesInNumber() {
        val debitCard = generateCardWithSpaceInNumber();
        debitOrder.PaymentByCard(debitCard);

        debitOrder.errorMessageByFieldIncorrectFormat();
    }

    // Негативные тест-кейсы на поле "Месяц"

    @Test
    void shouldDeclinedPaymentByCardWithEmptyMonth() {
        val invalidDebitCard = generateCardWithEmptyMonthField();
        debitOrder.PaymentByCard(invalidDebitCard);

        debitOrder.errorMessageByEmptyField();
    }

    @Test
    void shouldDeclinedPaymentByCardWithMonthUnderMin() {
        val invalidDebitCard = generateCardWithMonthUnderMin();
        debitOrder.PaymentByCard(invalidDebitCard);

        debitOrder.errorMessageByFieldIncorrectFormat();
    }

    @Test
    void shouldDeclinedPaymentByCardWithOneNumInMonth() {
        val invalidDebitCard = generateCardWithOneNumInMonth();
        debitOrder.PaymentByCard(invalidDebitCard);

        debitOrder.errorMessageByFieldIncorrectFormat();
    }

    @Test
    void shouldDeclinedPaymentByCardWithThreeNumInMonth() {
        val invalidDebitCard = generateCardWithThreeNumInMonth();
        debitOrder.PaymentByCard(invalidDebitCard);

        if (Integer.parseInt(invalidDebitCard.getCardMonth()) / 10 <= 12) {
            debitOrder.successfulPaymentMessage();
        } else {
            debitOrder.errorMessageByIncorrectFormatMonthField();
        }
    }

    @Test
    void shouldDeclinedPaymentByCardWithMonthUpperMax() {
        val invalidDebitCard = generateCardWithMonthUpperMax();
        debitOrder.PaymentByCard(invalidDebitCard);

        debitOrder.errorMessageByIncorrectFormatMonthField();
    }

    @Test
    void shouldDeclinedPaymentByCardWithWordInMonth() {
        val debitCard = generateCardWithWordInNumber();
        debitOrder.PaymentByCard(debitCard);
        debitOrder.errorMessageByEmptyField();
    }

    @Test
    void shouldDeclinedPaymentByCardWithSpecialCharacterInMonth() {
        val debitCard = generateCardWithSpecialCharacterInMonth();
        debitOrder.PaymentByCard(debitCard);
        debitOrder.errorMessageByEmptyField();
    }

    @Test
    void shouldDeclinedPaymentByCardWithSpacesInMonth() {
        val debitCard = generateCardWithSpaceInMonth();
        debitOrder.PaymentByCard(debitCard);
        debitOrder.errorMessageByFieldIncorrectFormat();
    }

    @Test
    void shouldDeclinedPaymentByCardWithOldMonth() {
        val invalidDebitCard = generateCardWithOldMonth();
        debitOrder.PaymentByCard(invalidDebitCard);
        debitOrder.errorMessageByIncorrectFormatMonthField();
    }

    // Негативные тест-кейсы на поле "Год"

    @Test
    void shouldDeclinedPaymentByCardWithEmptyYear() {
        val invalidDebitCard = generateCardWithEmptyYearField();
        debitOrder.PaymentByCard(invalidDebitCard);
        debitOrder.errorMessageByEmptyField();
    }

    @Test
    void shouldDeclinedPaymentByCardWithOldYear() {
        val invalidDebitCard = generateCardWithOldYear();
        debitOrder.PaymentByCard(invalidDebitCard);
        debitOrder.errorMessageByOldYear();
    }

    @Test
    void shouldDeclinedPaymentByCardWithOneNumInYear() {
        val invalidDebitCard = generateCardWithOneNumInYear();
        debitOrder.PaymentByCard(invalidDebitCard);
        debitOrder.errorMessageByFieldIncorrectFormat();
    }

    @Test
    void shouldDeclinedPaymentByCardWithThreeNumInYear() {
        val invalidDebitCard = generateCardWithThreeNumInYear();
        debitOrder.PaymentByCard(invalidDebitCard);

        if (Integer.parseInt(invalidDebitCard.getCardYear()) / 10 >= 20) {
            debitOrder.successfulPaymentMessage();
        } else
            debitOrder.errorMessageByOldYear();
    }

    @Test
    void shouldDeclinedPaymentByCardWithWordInYear() {
        val debitCard = generateCardWithWordInYear();
        debitOrder.PaymentByCard(debitCard);
        debitOrder.errorMessageByEmptyField();
    }

    @Test
    void shouldDeclinedPaymentByCardWithSpecialCharacterInYear() {
        val debitCard = generateCardWithSpecialCharacterInYear();
        debitOrder.PaymentByCard(debitCard);
        debitOrder.errorMessageByEmptyField();
    }

    @Test
    void shouldDeclinedPaymentByCardWithSpacesInYear() {
        val debitCard = generateCardWithSpaceInYear();
        debitOrder.PaymentByCard(debitCard);
        debitOrder.errorMessageByFieldIncorrectFormat();
    }

    // Негативные тест-кейсы на поле "Владелец"

    @Test
    void shouldDeclinedPaymentByCardWithEmptyOwner() {
        val debitCard = generateCardWithEmptyOwnerField();
        debitOrder.PaymentByCard(debitCard);
        debitOrder.errorMessageByEmptyField();
    }

    @Test
    void shouldDeclinedPaymentByCardWithOwnerUpperMax() {
        val debitCard = generateCardWithOwnerUpperMax31Symbols();
        debitOrder.PaymentByCard(debitCard);
        debitOrder.errorMessageByFieldIncorrectFormat();
    }

    @Test
    void shouldDeclinedPaymentByCardWithOwnerByLowerCase() {
        val debitCard = generateCardWithOwnerByLowerCase();
        debitOrder.PaymentByCard(debitCard);
        debitOrder.errorMessageByFieldIncorrectFormat();
    }

    @Test
    void shouldDeclinedPaymentByCardWithOwnerByUpperCase() {
        val debitCard = generateCardWithOwnerByUpperCase();
        debitOrder.PaymentByCard(debitCard);
        debitOrder.errorMessageByFieldIncorrectFormat();
    }

    @Test
    void shouldDeclinedPaymentByCardWithOwnerByRusName() {
        val debitCard = generateCardWithFullName("ru");
        debitOrder.PaymentByCard(debitCard);
        debitOrder.errorMessageByFieldIncorrectFormat();
    }

    @Test
    void shouldDeclinedPaymentByCardWithOwnerCnName() {
        val debitCard = generateCardWithFullName("zh-CN");
        debitOrder.PaymentByCard(debitCard);
        debitOrder.errorMessageByFieldIncorrectFormat();
    }

    @Test
    void shouldDeclinedPaymentByCardWithOwnerArabicName() {
        val debitCard = generateCardWithFullName("ar");
        debitOrder.PaymentByCard(debitCard);
        debitOrder.errorMessageByFieldIncorrectFormat();
    }

    @Test
    void shouldDeclinedPaymentByCardWithNumericOwner() {
        val debitCard = generateCardWithNumericOwner();
        debitOrder.PaymentByCard(debitCard);
        debitOrder.errorMessageByFieldIncorrectFormat();
    }

    @Test
    void shouldDeclinedPaymentByCardWithOwnerBySpacesBeforeText() {
        val debitCard = generateCardWithOwnerBySpacesBeforeText();
        debitOrder.PaymentByCard(debitCard);
        debitOrder.errorMessageByFieldIncorrectFormat();
    }

    @Test
    void shouldDeclinedPaymentByCardWithOwnerBySpacesAfterText() {
        val debitCard = generateCardWithOwnerBySpacesAfterText();
        debitOrder.PaymentByCard(debitCard);
        debitOrder.errorMessageByFieldIncorrectFormat();
    }

    @Test
    void shouldDeclinedPaymentByCardWithOwnerByMoreSpacesBetweenNameAndSurname() {
        val debitCard = generateCardWithOwnerByMoreSpacesBetweenNameAndSurname();
        debitOrder.PaymentByCard(debitCard);
        debitOrder.errorMessageByFieldIncorrectFormat();
    }

    @Test
    void shouldDeclinedPaymentByCardWithOwnerByHyphenInStartName() {
        val debitCard = generateCardWithOwnerByHyphenInStartName();
        debitOrder.PaymentByCard(debitCard);
        debitOrder.errorMessageByFieldIncorrectFormat();
    }

    @Test
    void shouldDeclinedPaymentByCardWithOwnerByHyphenInFinishName() {
        val debitCard = generateCardWithOwnerByHyphenInFinishName();
        debitOrder.PaymentByCard(debitCard);
        debitOrder.errorMessageByFieldIncorrectFormat();
    }

    // Негативные тест-кейсы на поле "CVC/CVV"

    @Test
    void shouldDeclinedPaymentByCardWithEmptyCVC() {
        val invalidDebitCard = generateCardWithEmptyCVCField();
        debitOrder.PaymentByCard(invalidDebitCard);
        debitOrder.errorMessageByEmptyField();
    }

    @Test
    void shouldDeclinedPaymentByCardWithOneNumInCVC() {
        val invalidDebitCard = generateCardWithOneNumInCVC();
        debitOrder.PaymentByCard(invalidDebitCard);
        debitOrder.errorMessageByFieldIncorrectFormat();
    }

    @Test
    void shouldDeclinedPaymentByCardWithTwoNumInCVC() {
        val invalidDebitCard = generateCardWithTwoNumInCVC();
        debitOrder.PaymentByCard(invalidDebitCard);
        debitOrder.errorMessageByFieldIncorrectFormat();
    }

    @Test
    void shouldDeclinedPaymentByCardWithWordInCVC() {
        val debitCard = generateCardWithWordInCVC();
        debitOrder.PaymentByCard(debitCard);
        debitOrder.errorMessageByEmptyField();
    }

    @Test
    void shouldDeclinedPaymentByCardWithSpecialCharacterInCVC() {
        val debitCard = generateCardWithSpecialCharacterInCVC();
        debitOrder.PaymentByCard(debitCard);
        debitOrder.errorMessageByEmptyField();
    }

    @Test
    void shouldDeclinedPaymentByCardWithSpacesInCVC() {
        val debitCard = generateCardWithSpaceInCVC();
        debitOrder.PaymentByCard(debitCard);
        debitOrder.errorMessageByFieldIncorrectFormat();
    }

}
