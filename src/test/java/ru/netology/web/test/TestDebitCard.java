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

public class TestDebitCard {
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
    void shouldPaymentByValidDebitCard() {
        val titlePage = open("http://localhost:8080", TitlePage.class);
        val debitOrder = titlePage.debitCardPayment();
        debitOrder.isVisibleDebitCard();

        val validDebitCard = generateValidCard();
        debitOrder.PaymentByCard(validDebitCard);

        debitOrder.successfulPaymentMessage();
    }

    @Test
    void shouldWriteToDataBaseApprovedPaymentByDebitCard() throws InterruptedException {
        val titlePage = open("http://localhost:8080", TitlePage.class);
        val debitOrder = titlePage.debitCardPayment();
        debitOrder.isVisibleDebitCard();

        val validDebitCard = generateValidCard();
        debitOrder.PaymentByCard(validDebitCard);

        Thread.sleep(10000);
        assertEquals("APPROVED", searchOperationStatusByDebitCard());
        assertNotNull(searchOperationByOrderTableForDebitCard());
    }

    @Test
    void shouldPaymentByValidDebitCardWithMaxValidCardOwner() {
        val titlePage = open("http://localhost:8080", TitlePage.class);
        val debitOrder = titlePage.debitCardPayment();
        debitOrder.isVisibleDebitCard();

        val validDebitCard = generateValidCardWithMaxValidCardOwner();
        debitOrder.PaymentByCard(validDebitCard);

        debitOrder.successfulPaymentMessage();
    }

    @Test
    void shouldPaymentByValidDebitCardWithMinValidCardOwner() {
        val titlePage = open("http://localhost:8080", TitlePage.class);
        val debitOrder = titlePage.debitCardPayment();
        debitOrder.isVisibleDebitCard();

        val validDebitCard = generateValidCardWithMinValidCardOwner();
        debitOrder.PaymentByCard(validDebitCard);

        debitOrder.successfulPaymentMessage();
    }

    @Test
    void shouldPaymentByCardWithOwnerByDoubleSurname() {
        val titlePage = open("http://localhost:8080", TitlePage.class);
        val debitOrder = titlePage.debitCardPayment();
        debitOrder.isVisibleDebitCard();

        val debitCard = generateCardWithOwnerByDoubleSurname();
        debitOrder.PaymentByCard(debitCard);

        debitOrder.successfulPaymentMessage();
    }

    // Негативные тест-кейсы на поле "Номер карты"

    @Test
    void shouldDeclinedPaymentByCardWithExistIncorrectNumber() {
        val titlePage = open("http://localhost:8080", TitlePage.class);
        val debitOrder = titlePage.debitCardPayment();
        debitOrder.isVisibleDebitCard();

        val invalidDebitCard = generateExistInvalidCardNumberForDeclinedOperation();
        debitOrder.PaymentByCard(invalidDebitCard);

        debitOrder.errorPaymentMessage();
    }

    @Test
    void shouldWriteToDataBaseDeclinedPaymentByDebitCard() throws InterruptedException {
        val titlePage = open("http://localhost:8080", TitlePage.class);
        val debitOrder = titlePage.debitCardPayment();
        debitOrder.isVisibleDebitCard();

        val invalidDebitCard = generateExistInvalidCardNumberForDeclinedOperation();
        debitOrder.PaymentByCard(invalidDebitCard);

        Thread.sleep(10000);
        assertEquals("DECLINED", searchOperationStatusByDebitCard());
        assertNotNull(searchOperationByOrderTableForDebitCard());
    }

    @Test
    void shouldDeclinedPaymentByCardWithNotExistNumber() {
        val titlePage = open("http://localhost:8080", TitlePage.class);
        val debitOrder = titlePage.debitCardPayment();
        debitOrder.isVisibleDebitCard();

        val invalidDebitCard = generateNoExistCardNumberForDeclinedOperation();
        debitOrder.PaymentByCard(invalidDebitCard);

        debitOrder.errorPaymentMessage();
    }

    @Test
    void shouldDeclinedPaymentByCardWithEmptyNumber() {
        val titlePage = open("http://localhost:8080", TitlePage.class);
        val debitOrder = titlePage.debitCardPayment();
        debitOrder.isVisibleDebitCard();

        val invalidDebitCard = generateCardWithEmptyNumberField();
        debitOrder.PaymentByCard(invalidDebitCard);

        debitOrder.errorMessageByEmptyField();
    }

    @Test
    void shouldDeclinedPaymentByCardWithNoFullNumber() {
        val titlePage = open("http://localhost:8080", TitlePage.class);
        val debitOrder = titlePage.debitCardPayment();
        debitOrder.isVisibleDebitCard();

        val debitCard = generateCardWithNoFullNumber();
        debitOrder.PaymentByCard(debitCard);

        debitOrder.errorMessageByFieldIncorrectFormat();
    }

    @Test
    void shouldDeclinedPaymentByCardWithWordInNumber() {
        val titlePage = open("http://localhost:8080", TitlePage.class);
        val debitOrder = titlePage.debitCardPayment();
        debitOrder.isVisibleDebitCard();

        val debitCard = generateCardWithWordInNumber();
        debitOrder.PaymentByCard(debitCard);

        debitOrder.errorMessageByEmptyField();
    }

    @Test
    void shouldDeclinedPaymentByCardWithSpecialCharacterInNumber() {
        val titlePage = open("http://localhost:8080", TitlePage.class);
        val debitOrder = titlePage.debitCardPayment();
        debitOrder.isVisibleDebitCard();

        val debitCard = generateCardWithSpecialCharacterInNumber();
        debitOrder.PaymentByCard(debitCard);

        debitOrder.errorMessageByEmptyField();
    }

    @Test
    void shouldDeclinedPaymentByCardWithSpacesInNumber() {
        val titlePage = open("http://localhost:8080", TitlePage.class);
        val debitOrder = titlePage.debitCardPayment();
        debitOrder.isVisibleDebitCard();

        val debitCard = generateCardWithSpaceInNumber();
        debitOrder.PaymentByCard(debitCard);

        debitOrder.errorMessageByFieldIncorrectFormat();
    }

    // Негативные тест-кейсы на поле "Месяц"

    @Test
    void shouldDeclinedPaymentByCardWithEmptyMonth() {
        val titlePage = open("http://localhost:8080", TitlePage.class);
        val debitOrder = titlePage.debitCardPayment();
        debitOrder.isVisibleDebitCard();

        val invalidDebitCard = generateCardWithEmptyMonthField();
        debitOrder.PaymentByCard(invalidDebitCard);

        debitOrder.errorMessageByEmptyField();
    }

    @Test
    void shouldDeclinedPaymentByCardWithMonthUnderMin() {
        val titlePage = open("http://localhost:8080", TitlePage.class);
        val debitOrder = titlePage.debitCardPayment();
        debitOrder.isVisibleDebitCard();

        val invalidDebitCard = generateCardWithMonthUnderMin();
        debitOrder.PaymentByCard(invalidDebitCard);

        debitOrder.errorMessageByFieldIncorrectFormat();
    }

    @Test
    void shouldDeclinedPaymentByCardWithOneNumInMonth() {
        val titlePage = open("http://localhost:8080", TitlePage.class);
        val debitOrder = titlePage.debitCardPayment();
        debitOrder.isVisibleDebitCard();

        val invalidDebitCard = generateCardWithOneNumInMonth();
        debitOrder.PaymentByCard(invalidDebitCard);

        debitOrder.errorMessageByFieldIncorrectFormat();
    }

    @Test
    void shouldDeclinedPaymentByCardWithThreeNumInMonth() {
        val titlePage = open("http://localhost:8080", TitlePage.class);
        val debitOrder = titlePage.debitCardPayment();
        debitOrder.isVisibleDebitCard();

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
        val titlePage = open("http://localhost:8080", TitlePage.class);
        val debitOrder = titlePage.debitCardPayment();
        debitOrder.isVisibleDebitCard();

        val invalidDebitCard = generateCardWithMonthUpperMax();
        debitOrder.PaymentByCard(invalidDebitCard);

        debitOrder.errorMessageByIncorrectFormatMonthField();
    }

    @Test
    void shouldDeclinedPaymentByCardWithWordInMonth() {
        val titlePage = open("http://localhost:8080", TitlePage.class);
        val debitOrder = titlePage.debitCardPayment();
        debitOrder.isVisibleDebitCard();

        val debitCard = generateCardWithWordInNumber();
        debitOrder.PaymentByCard(debitCard);
        debitOrder.errorMessageByEmptyField();
    }

    @Test
    void shouldDeclinedPaymentByCardWithSpecialCharacterInMonth() {
        val titlePage = open("http://localhost:8080", TitlePage.class);
        val debitOrder = titlePage.debitCardPayment();
        debitOrder.isVisibleDebitCard();

        val debitCard = generateCardWithSpecialCharacterInMonth();
        debitOrder.PaymentByCard(debitCard);
        debitOrder.errorMessageByEmptyField();
    }

    @Test
    void shouldDeclinedPaymentByCardWithSpacesInMonth() {
        val titlePage = open("http://localhost:8080", TitlePage.class);
        val debitOrder = titlePage.debitCardPayment();
        debitOrder.isVisibleDebitCard();

        val debitCard = generateCardWithSpaceInMonth();
        debitOrder.PaymentByCard(debitCard);
        debitOrder.errorMessageByFieldIncorrectFormat();
    }

    @Test
    void shouldDeclinedPaymentByCardWithOldMonth() {
        val titlePage = open("http://localhost:8080", TitlePage.class);
        val debitOrder = titlePage.debitCardPayment();
        debitOrder.isVisibleDebitCard();

        val invalidDebitCard = generateCardWithOldMonth();
        debitOrder.PaymentByCard(invalidDebitCard);
        debitOrder.errorMessageByIncorrectFormatMonthField();
    }

    // Негативные тест-кейсы на поле "Год"

    @Test
    void shouldDeclinedPaymentByCardWithEmptyYear() {
        val titlePage = open("http://localhost:8080", TitlePage.class);
        val debitOrder = titlePage.debitCardPayment();
        debitOrder.isVisibleDebitCard();

        val invalidDebitCard = generateCardWithEmptyYearField();
        debitOrder.PaymentByCard(invalidDebitCard);
        debitOrder.errorMessageByEmptyField();
    }

    @Test
    void shouldDeclinedPaymentByCardWithOldYear() {
        val titlePage = open("http://localhost:8080", TitlePage.class);
        val debitOrder = titlePage.debitCardPayment();
        debitOrder.isVisibleDebitCard();

        val invalidDebitCard = generateCardWithOldYear();
        debitOrder.PaymentByCard(invalidDebitCard);
        debitOrder.errorMessageByOldYear();
    }

    @Test
    void shouldDeclinedPaymentByCardWithOneNumInYear() {
        val titlePage = open("http://localhost:8080", TitlePage.class);
        val debitOrder = titlePage.debitCardPayment();
        debitOrder.isVisibleDebitCard();

        val invalidDebitCard = generateCardWithOneNumInYear();
        debitOrder.PaymentByCard(invalidDebitCard);
        debitOrder.errorMessageByFieldIncorrectFormat();
    }

    @Test
    void shouldDeclinedPaymentByCardWithThreeNumInYear() {
        val titlePage = open("http://localhost:8080", TitlePage.class);
        val debitOrder = titlePage.debitCardPayment();
        debitOrder.isVisibleDebitCard();

        val invalidDebitCard = generateCardWithThreeNumInYear();
        debitOrder.PaymentByCard(invalidDebitCard);

        if (Integer.parseInt(invalidDebitCard.getCardYear()) / 10 >= 20) {
            debitOrder.successfulPaymentMessage();
        } else
            debitOrder.errorMessageByOldYear();
    }

    @Test
    void shouldDeclinedPaymentByCardWithWordInYear() {
        val titlePage = open("http://localhost:8080", TitlePage.class);
        val debitOrder = titlePage.debitCardPayment();
        debitOrder.isVisibleDebitCard();

        val debitCard = generateCardWithWordInYear();
        debitOrder.PaymentByCard(debitCard);
        debitOrder.errorMessageByEmptyField();
    }

    @Test
    void shouldDeclinedPaymentByCardWithSpecialCharacterInYear() {
        val titlePage = open("http://localhost:8080", TitlePage.class);
        val debitOrder = titlePage.debitCardPayment();
        debitOrder.isVisibleDebitCard();

        val debitCard = generateCardWithSpecialCharacterInYear();
        debitOrder.PaymentByCard(debitCard);
        debitOrder.errorMessageByEmptyField();
    }

    @Test
    void shouldDeclinedPaymentByCardWithSpacesInYear() {
        val titlePage = open("http://localhost:8080", TitlePage.class);
        val debitOrder = titlePage.debitCardPayment();
        debitOrder.isVisibleDebitCard();

        val debitCard = generateCardWithSpaceInYear();
        debitOrder.PaymentByCard(debitCard);
        debitOrder.errorMessageByFieldIncorrectFormat();
    }

    // Негативные тест-кейсы на поле "Владелец"

    @Test
    void shouldDeclinedPaymentByCardWithEmptyOwner() {
        val titlePage = open("http://localhost:8080", TitlePage.class);
        val debitOrder = titlePage.debitCardPayment();
        debitOrder.isVisibleDebitCard();

        val debitCard = generateCardWithEmptyOwnerField();
        debitOrder.PaymentByCard(debitCard);
        debitOrder.errorMessageByEmptyField();
    }

    @Test
    void shouldDeclinedPaymentByCardWithOwnerUpperMax() {
        val titlePage = open("http://localhost:8080", TitlePage.class);
        val debitOrder = titlePage.debitCardPayment();
        debitOrder.isVisibleDebitCard();

        val debitCard = generateCardWithOwnerUpperMax31Symbols();
        debitOrder.PaymentByCard(debitCard);
        debitOrder.errorMessageByFieldIncorrectFormat();
    }

    @Test
    void shouldDeclinedPaymentByCardWithOwnerByLowerCase() {
        val titlePage = open("http://localhost:8080", TitlePage.class);
        val debitOrder = titlePage.debitCardPayment();
        debitOrder.isVisibleDebitCard();

        val debitCard = generateCardWithOwnerByLowerCase();
        debitOrder.PaymentByCard(debitCard);
        debitOrder.errorMessageByFieldIncorrectFormat();
    }

    @Test
    void shouldDeclinedPaymentByCardWithOwnerByUpperCase() {
        val titlePage = open("http://localhost:8080", TitlePage.class);
        val debitOrder = titlePage.debitCardPayment();
        debitOrder.isVisibleDebitCard();

        val debitCard = generateCardWithOwnerByUpperCase();
        debitOrder.PaymentByCard(debitCard);
        debitOrder.errorMessageByFieldIncorrectFormat();
    }

    @Test
    void shouldDeclinedPaymentByCardWithOwnerByRusName() {
        val titlePage = open("http://localhost:8080", TitlePage.class);
        val debitOrder = titlePage.debitCardPayment();
        debitOrder.isVisibleDebitCard();

        val debitCard = generateCardWithFullName("ru");
        debitOrder.PaymentByCard(debitCard);
        debitOrder.errorMessageByFieldIncorrectFormat();
    }

    @Test
    void shouldDeclinedPaymentByCardWithOwnerCnName() {
        val titlePage = open("http://localhost:8080", TitlePage.class);
        val debitOrder = titlePage.debitCardPayment();
        debitOrder.isVisibleDebitCard();

        val debitCard = generateCardWithFullName("zh-CN");
        debitOrder.PaymentByCard(debitCard);
        debitOrder.errorMessageByFieldIncorrectFormat();
    }

    @Test
    void shouldDeclinedPaymentByCardWithOwnerArabicName() {
        val titlePage = open("http://localhost:8080", TitlePage.class);
        val debitOrder = titlePage.debitCardPayment();
        debitOrder.isVisibleDebitCard();

        val debitCard = generateCardWithFullName("ar");
        debitOrder.PaymentByCard(debitCard);
        debitOrder.errorMessageByFieldIncorrectFormat();
    }

    @Test
    void shouldDeclinedPaymentByCardWithNumericOwner() {
        val titlePage = open("http://localhost:8080", TitlePage.class);
        val debitOrder = titlePage.debitCardPayment();
        debitOrder.isVisibleDebitCard();

        val debitCard = generateCardWithNumericOwner();
        debitOrder.PaymentByCard(debitCard);
        debitOrder.errorMessageByFieldIncorrectFormat();
    }

    @Test
    void shouldDeclinedPaymentByCardWithOwnerBySpacesBeforeText() {
        val titlePage = open("http://localhost:8080", TitlePage.class);
        val debitOrder = titlePage.debitCardPayment();
        debitOrder.isVisibleDebitCard();

        val debitCard = generateCardWithOwnerBySpacesBeforeText();
        debitOrder.PaymentByCard(debitCard);
        debitOrder.errorMessageByFieldIncorrectFormat();
    }

    @Test
    void shouldDeclinedPaymentByCardWithOwnerBySpacesAfterText() {
        val titlePage = open("http://localhost:8080", TitlePage.class);
        val debitOrder = titlePage.debitCardPayment();
        debitOrder.isVisibleDebitCard();

        val debitCard = generateCardWithOwnerBySpacesAfterText();
        debitOrder.PaymentByCard(debitCard);
        debitOrder.errorMessageByFieldIncorrectFormat();
    }

    @Test
    void shouldDeclinedPaymentByCardWithOwnerByMoreSpacesBetweenNameAndSurname() {
        val titlePage = open("http://localhost:8080", TitlePage.class);
        val debitOrder = titlePage.debitCardPayment();
        debitOrder.isVisibleDebitCard();

        val debitCard = generateCardWithOwnerByMoreSpacesBetweenNameAndSurname();
        debitOrder.PaymentByCard(debitCard);
        debitOrder.errorMessageByFieldIncorrectFormat();
    }

    @Test
    void shouldDeclinedPaymentByCardWithOwnerByHyphenInStartName() {
        val titlePage = open("http://localhost:8080", TitlePage.class);
        val debitOrder = titlePage.debitCardPayment();
        debitOrder.isVisibleDebitCard();

        val debitCard = generateCardWithOwnerByHyphenInStartName();
        debitOrder.PaymentByCard(debitCard);
        debitOrder.errorMessageByFieldIncorrectFormat();
    }

    @Test
    void shouldDeclinedPaymentByCardWithOwnerByHyphenInFinishName() {
        val titlePage = open("http://localhost:8080", TitlePage.class);
        val debitOrder = titlePage.debitCardPayment();
        debitOrder.isVisibleDebitCard();

        val debitCard = generateCardWithOwnerByHyphenInFinishName();
        debitOrder.PaymentByCard(debitCard);
        debitOrder.errorMessageByFieldIncorrectFormat();
    }

    // Негативные тест-кейсы на поле "CVC/CVV"

    @Test
    void shouldDeclinedPaymentByCardWithEmptyCVC() {
        val titlePage = open("http://localhost:8080", TitlePage.class);
        val debitOrder = titlePage.debitCardPayment();
        debitOrder.isVisibleDebitCard();

        val invalidDebitCard = generateCardWithEmptyCVCField();
        debitOrder.PaymentByCard(invalidDebitCard);
        debitOrder.errorMessageByEmptyField();
    }

    @Test
    void shouldDeclinedPaymentByCardWithOneNumInCVC() {
        val titlePage = open("http://localhost:8080", TitlePage.class);
        val debitOrder = titlePage.debitCardPayment();
        debitOrder.isVisibleDebitCard();

        val invalidDebitCard = generateCardWithOneNumInCVC();
        debitOrder.PaymentByCard(invalidDebitCard);
        debitOrder.errorMessageByFieldIncorrectFormat();
    }

    @Test
    void shouldDeclinedPaymentByCardWithTwoNumInCVC() {
        val titlePage = open("http://localhost:8080", TitlePage.class);
        val debitOrder = titlePage.debitCardPayment();
        debitOrder.isVisibleDebitCard();

        val invalidDebitCard = generateCardWithTwoNumInCVC();
        debitOrder.PaymentByCard(invalidDebitCard);
        debitOrder.errorMessageByFieldIncorrectFormat();
    }

    @Test
    void shouldDeclinedPaymentByCardWithWordInCVC() {
        val titlePage = open("http://localhost:8080", TitlePage.class);
        val debitOrder = titlePage.debitCardPayment();
        debitOrder.isVisibleDebitCard();

        val debitCard = generateCardWithWordInCVC();
        debitOrder.PaymentByCard(debitCard);
        debitOrder.errorMessageByEmptyField();
    }

    @Test
    void shouldDeclinedPaymentByCardWithSpecialCharacterInCVC() {
        val titlePage = open("http://localhost:8080", TitlePage.class);
        val debitOrder = titlePage.debitCardPayment();
        debitOrder.isVisibleDebitCard();

        val debitCard = generateCardWithSpecialCharacterInCVC();
        debitOrder.PaymentByCard(debitCard);
        debitOrder.errorMessageByEmptyField();
    }

    @Test
    void shouldDeclinedPaymentByCardWithSpacesInCVC() {
        val titlePage = open("http://localhost:8080", TitlePage.class);
        val debitOrder = titlePage.debitCardPayment();
        debitOrder.isVisibleDebitCard();

        val debitCard = generateCardWithSpaceInCVC();
        debitOrder.PaymentByCard(debitCard);
        debitOrder.errorMessageByFieldIncorrectFormat();
    }

}
