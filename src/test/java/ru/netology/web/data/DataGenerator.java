package ru.netology.web.data;

import com.github.javafaker.Faker;
import lombok.Data;
import lombok.val;
import org.apache.commons.dbutils.QueryRunner;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Random;

@Data
public class DataGenerator {

    DataGenerator() {
    }

    // Данные для позитивных тест-кейсов.

    // Проверяем граничные значения на всех полях.
    public static CardInfo generateValidCard() {
        CardInfo validCard = new CardInfo(
                "4444444444444441",
                "Nikita Ivanov",
                generateYearNow(),
                generateValidCardMonth(),
                "123"
        );
        return validCard;
    }

    // Считаю допустимым диапазон 1-30 Английских символов
    public static CardInfo generateValidCardWithMaxValidCardOwner() {
        CardInfo validCard = new CardInfo(
                "4444444444444441",
                "SSSSSsssssSSSSSsssssSSSSSsssss",
                "21",
                "12",
                "999"
        );
        return validCard;
    }

    public static CardInfo generateValidCardWithMinValidCardOwner() {
        CardInfo validCard = new CardInfo(
                "4444444444444441",
                "Y",
                "25",
                "01",
                "000"
        );
        return validCard;
    }

    public static CardInfo generateCardWithOwnerByDoubleSurname() {
        CardInfo invalidCard = new CardInfo(
                generateValidCardNumber(),
                getNameWithDoubleSurname(),
                generateValidCardYear(),
                generateValidCardMonth(),
                generateValidCardCvcCode()
        );
        return invalidCard;
    }

    // Данные для негативных тест-кейсов на поле "Номер карты"

    public static CardInfo generateExistInvalidCardNumberForDeclinedOperation() {
        CardInfo invalidCard = new CardInfo(
                "4444444444444442",
                generateValidOwner(),
                generateValidCardYear(),
                generateValidCardMonth(),
                generateValidCardCvcCode()
        );
        return invalidCard;
    }

    public static CardInfo generateNoExistCardNumberForDeclinedOperation() {
        CardInfo invalidCard = new CardInfo(
                "3333333333333333",
                generateValidOwner(),
                generateValidCardYear(),
                generateValidCardMonth(),
                generateValidCardCvcCode()
        );
        return invalidCard;
    }

    public static CardInfo generateCardWithEmptyNumberField() {
        CardInfo invalidCard = new CardInfo(
                "",
                generateValidOwner(),
                generateValidCardYear(),
                generateValidCardMonth(),
                generateValidCardCvcCode()
        );
        return invalidCard;
    }

    public static CardInfo generateCardWithNoFullNumber() {
        CardInfo invalidCard = new CardInfo(
                generateNoFullCardNumber(),
                generateValidOwner(),
                generateValidCardYear(),
                generateValidCardMonth(),
                generateValidCardCvcCode()
        );
        return invalidCard;
    }

    public static CardInfo generateCardWithWordInNumber() {
        CardInfo invalidCard = new CardInfo(
                "тесттесттесттест",
                generateValidOwner(),
                generateValidCardYear(),
                generateValidCardMonth(),
                generateValidCardCvcCode()
        );
        return invalidCard;
    }

    public static CardInfo generateCardWithSpecialCharacterInNumber() {
        CardInfo invalidCard = new CardInfo(
                getSpecialCharacter(),
                generateValidOwner(),
                generateValidCardYear(),
                generateValidCardMonth(),
                generateValidCardCvcCode()
        );
        return invalidCard;
    }

    public static CardInfo generateCardWithSpaceInNumber() {
        CardInfo invalidCard = new CardInfo(
                generateCardNumberWithSpace(),
                generateValidOwner(),
                generateValidCardYear(),
                generateValidCardMonth(),
                generateValidCardCvcCode()
        );
        return invalidCard;
    }

    // Данные для негативных тест-кейсов на поле "Месяц"

    public static CardInfo generateCardWithEmptyMonthField() {
        CardInfo invalidCard = new CardInfo(
                generateValidCardNumber(),
                generateValidOwner(),
                generateValidCardYear(),
                "",
                generateValidCardCvcCode()
        );
        return invalidCard;
    }

    public static CardInfo generateCardWithOldMonth() {
        CardInfo invalidCard = new CardInfo(
                generateValidCardNumber(),
                generateValidOwner(),
                generateYearNow(),
                generateOldCardMonth(),
                generateValidCardCvcCode()
        );
        return invalidCard;
    }


    public static CardInfo generateCardWithMonthUnderMin() {
        CardInfo invalidCard = new CardInfo(
                generateValidCardNumber(),
                generateValidOwner(),
                generateValidCardYear(),
                "00",
                generateValidCardCvcCode()
        );
        return invalidCard;
    }

    public static CardInfo generateCardWithOneNumInMonth() {
        CardInfo invalidCard = new CardInfo(
                generateValidCardNumber(),
                generateValidOwner(),
                generateValidCardYear(),
                generateOneNum(),
                generateValidCardCvcCode()
        );
        return invalidCard;
    }

    public static CardInfo generateCardWithThreeNumInMonth() {
        CardInfo invalidCard = new CardInfo(
                generateValidCardNumber(),
                generateValidOwner(),
                generateValidCardYear(),
                generateThreeNum(),
                generateValidCardCvcCode()
        );
        return invalidCard;
    }

    public static CardInfo generateCardWithMonthUpperMax() {
        CardInfo invalidCard = new CardInfo(
                generateValidCardNumber(),
                generateValidOwner(),
                generateValidCardYear(),
                generateInvalidCardMonthUpperMax(),
                generateValidCardCvcCode()
        );
        return invalidCard;
    }

    public static CardInfo generateCardWithWordInMonth() {
        CardInfo invalidCard = new CardInfo(
                generateValidCardNumber(),
                generateValidOwner(),
                generateValidCardYear(),
                "тесттесттесттест",
                generateValidCardCvcCode()
        );
        return invalidCard;
    }

    public static CardInfo generateCardWithSpecialCharacterInMonth() {
        CardInfo invalidCard = new CardInfo(
                generateValidCardNumber(),
                generateValidOwner(),
                generateValidCardYear(),
                getSpecialCharacter(),
                generateValidCardCvcCode()
        );
        return invalidCard;
    }

    public static CardInfo generateCardWithSpaceInMonth() {
        CardInfo invalidCard = new CardInfo(
                generateValidCardNumber(),
                generateValidOwner(),
                generateValidCardYear(),
                generateOneNumWithSpace(),
                generateValidCardCvcCode()
        );
        return invalidCard;
    }

    // Данные для негативных тест-кейсов на поле "Год"

    public static CardInfo generateCardWithEmptyYearField() {
        CardInfo invalidCard = new CardInfo(
                generateValidCardNumber(),
                generateValidOwner(),
                "",
                generateValidCardMonth(),
                generateValidCardCvcCode()
        );
        return invalidCard;
    }

    public static CardInfo generateCardWithOldYear() {
        CardInfo invalidCard = new CardInfo(
                generateValidCardNumber(),
                generateValidOwner(),
                generateOldYearCard(),
                generateValidCardMonth(),
                generateValidCardCvcCode()
        );
        return invalidCard;
    }

    public static CardInfo generateCardWithOneNumInYear() {
        CardInfo invalidCard = new CardInfo(
                generateValidCardNumber(),
                generateValidOwner(),
                generateOneNum(),
                generateValidCardMonth(),
                generateValidCardCvcCode()
        );
        return invalidCard;
    }

    public static CardInfo generateCardWithThreeNumInYear() {
        CardInfo invalidCard = new CardInfo(
                generateValidCardNumber(),
                generateValidOwner(),
                generateThreeNum(),
                generateValidCardMonth(),
                generateValidCardCvcCode()
        );
        return invalidCard;
    }

    public static CardInfo generateCardWithWordInYear() {
        CardInfo invalidCard = new CardInfo(
                generateValidCardNumber(),
                generateValidOwner(),
                "тесттесттесттест",
                generateValidCardMonth(),
                generateValidCardCvcCode()
        );
        return invalidCard;
    }

    public static CardInfo generateCardWithSpecialCharacterInYear() {
        CardInfo invalidCard = new CardInfo(
                generateValidCardNumber(),
                generateValidOwner(),
                getSpecialCharacter(),
                generateValidCardMonth(),
                generateValidCardCvcCode()
        );
        return invalidCard;
    }

    public static CardInfo generateCardWithSpaceInYear() {
        CardInfo invalidCard = new CardInfo(
                generateValidCardNumber(),
                generateValidOwner(),
                generateOneNumWithSpace(),
                generateValidCardMonth(),
                generateValidCardCvcCode()
        );
        return invalidCard;
    }

    // Данные для негативных тест-кейсов на поле "Владелец"

    public static CardInfo generateCardWithEmptyOwnerField() {
        CardInfo invalidCard = new CardInfo(
                generateValidCardNumber(),
                "",
                generateValidCardYear(),
                generateValidCardMonth(),
                generateValidCardCvcCode()
        );
        return invalidCard;
    }

    public static CardInfo generateCardWithOwnerUpperMax31Symbols() {
        CardInfo invalidCard = new CardInfo(
                generateValidCardNumber(),
                "SSSSSsssssSSSSSsssssSSSSSsssssS",
                generateValidCardYear(),
                generateValidCardMonth(),
                generateValidCardCvcCode()
        );
        return invalidCard;
    }

    public static CardInfo generateCardWithOwnerByLowerCase() {
        CardInfo invalidCard = new CardInfo(
                generateValidCardNumber(),
                generateCardOwnerByLowerCase(),
                generateValidCardYear(),
                generateValidCardMonth(),
                generateValidCardCvcCode()
        );
        return invalidCard;
    }

    public static CardInfo generateCardWithOwnerByUpperCase() {
        CardInfo invalidCard = new CardInfo(
                generateValidCardNumber(),
                generateCardOwnerByUpperCase(),
                generateValidCardYear(),
                generateValidCardMonth(),
                generateValidCardCvcCode()
        );
        return invalidCard;
    }

    public static CardInfo generateCardWithFullName(String locale) {
        CardInfo invalidCard = new CardInfo(
                generateValidCardNumber(),
                generateFullNameCardOwner(locale),
                generateValidCardYear(),
                generateValidCardMonth(),
                generateValidCardCvcCode()
        );
        return invalidCard;
    }

    public static CardInfo generateCardWithNumericOwner() {
        CardInfo invalidCard = new CardInfo(
                generateValidCardNumber(),
                getNumericOwner(),
                generateValidCardYear(),
                generateValidCardMonth(),
                generateValidCardCvcCode()
        );
        return invalidCard;
    }

    public static CardInfo generateCardWithOwnerBySpacesBeforeText() {
        CardInfo invalidCard = new CardInfo(
                generateValidCardNumber(),
                getNameWithSpacesBeforeText(),
                generateValidCardYear(),
                generateValidCardMonth(),
                generateValidCardCvcCode()
        );
        return invalidCard;
    }

    public static CardInfo generateCardWithOwnerBySpacesAfterText() {
        CardInfo invalidCard = new CardInfo(
                generateValidCardNumber(),
                getNameWithSpacesAfterText(),
                generateValidCardYear(),
                generateValidCardMonth(),
                generateValidCardCvcCode()
        );
        return invalidCard;
    }

    public static CardInfo generateCardWithOwnerByMoreSpacesBetweenNameAndSurname() {
        CardInfo invalidCard = new CardInfo(
                generateValidCardNumber(),
                getNameWithMoreSpacesBetweenNameAndSurname(),
                generateValidCardYear(),
                generateValidCardMonth(),
                generateValidCardCvcCode()
        );
        return invalidCard;
    }

    public static CardInfo generateCardWithOwnerByHyphenInStartName() {
        CardInfo invalidCard = new CardInfo(
                generateValidCardNumber(),
                getNameWithHyphenInStartName(),
                generateValidCardYear(),
                generateValidCardMonth(),
                generateValidCardCvcCode()
        );
        return invalidCard;
    }

    public static CardInfo generateCardWithOwnerByHyphenInFinishName() {
        CardInfo invalidCard = new CardInfo(
                generateValidCardNumber(),
                getNameWithHyphenInFinishName(),
                generateValidCardYear(),
                generateValidCardMonth(),
                generateValidCardCvcCode()
        );
        return invalidCard;
    }

    // Данные для негативных тест-кейсов на поле "CVC/CVV"

    public static CardInfo generateCardWithEmptyCVCField() {
        CardInfo invalidCard = new CardInfo(
                generateValidCardNumber(),
                generateValidOwner(),
                generateValidCardYear(),
                generateValidCardMonth(),
                ""
        );
        return invalidCard;
    }

    public static CardInfo generateCardWithOneNumInCVC() {
        CardInfo invalidCard = new CardInfo(
                generateValidCardNumber(),
                generateValidOwner(),
                generateValidCardYear(),
                generateValidCardMonth(),
                generateOneNum()
        );
        return invalidCard;
    }

    public static CardInfo generateCardWithTwoNumInCVC() {
        CardInfo invalidCard = new CardInfo(
                generateValidCardNumber(),
                generateValidOwner(),
                generateValidCardYear(),
                generateValidCardMonth(),
                generateTwoNum()
        );
        return invalidCard;
    }

    public static CardInfo generateCardWithWordInCVC() {
        CardInfo invalidCard = new CardInfo(
                generateValidCardNumber(),
                generateValidOwner(),
                generateValidCardYear(),
                generateValidCardMonth(),
                "test"
        );
        return invalidCard;
    }

    public static CardInfo generateCardWithSpecialCharacterInCVC() {
        CardInfo invalidCard = new CardInfo(
                generateValidCardNumber(),
                generateValidOwner(),
                generateValidCardYear(),
                generateValidCardMonth(),
                getSpecialCharacter()
        );
        return invalidCard;
    }

    public static CardInfo generateCardWithSpaceInCVC() {
        CardInfo invalidCard = new CardInfo(
                generateValidCardNumber(),
                generateValidOwner(),
                generateValidCardYear(),
                generateValidCardMonth(),
                generateOneNumWithSpace()
        );
        return invalidCard;
    }

    // Атомарные дата-сеты

    public static String getNameWithDoubleSurname() {
        Faker faker = new Faker(new Locale("en-AU"));
        return faker.name().firstName() + " " + faker.name().lastName() + "-" + faker.name().lastName();
    }

    public static String getNameWithHyphenInFinishName() {
        Faker faker = new Faker(new Locale("en-AU"));
        return faker.name().firstName() + " " + faker.name().lastName() + "-";
    }

    public static String getNameWithHyphenInStartName() {
        Faker faker = new Faker(new Locale("en-AU"));
        return "-" + faker.name().firstName();
    }

    public static String getNameWithMoreSpacesBetweenNameAndSurname() {
        Faker faker = new Faker(new Locale("en-AU"));
        return faker.name().firstName() + "      " + faker.name().lastName();
    }

    public static String getNameWithSpacesAfterText() {
        Faker faker = new Faker(new Locale("en-AU"));
        return faker.name().firstName() + "      ";
    }

    public static String getNameWithSpacesBeforeText() {
        Faker faker = new Faker(new Locale("en-AU"));
        return "      " + faker.name().firstName();
    }

    public static String getNumericOwner() {
        Faker faker = new Faker(new Locale("ru"));
        return faker.random().nextInt(0, 1000).toString();
    }

    private static String generateFullNameCardOwner(String locale) {
        Faker faker = new Faker(new Locale(locale));
        return faker.name().fullName();
    }

    private static String generateCardOwnerByLowerCase() {
        Faker faker = new Faker(new Locale("en-AU"));

        return faker.name().firstName().toLowerCase();
    }

    private static String generateCardOwnerByUpperCase() {
        Faker faker = new Faker(new Locale("en-AU"));

        return faker.name().firstName().toUpperCase();
    }

    private static String generateOldCardMonth() {
        Integer monthNow = LocalDate.now().getMonthValue();

        if (monthNow == 1) {
            return Integer.toString(monthNow);
        } else {
            --monthNow;
            if (monthNow <= 9) {
                return "0" + monthNow;
            }
            return Integer.toString(monthNow);
        }

    }

    private static String generateOldYearCard() {
        Faker faker = new Faker(new Locale("ru"));
        Integer yearNow = LocalDate.now().getYear() % 100;
        int month = faker.random().nextInt(0, yearNow);

        if (month < 10) {
            return "0" + month;
        }
        return Integer.toString(month);
    }

    public static String generateCardNumberWithSpace() {
        Faker faker = new Faker(new Locale("ru"));
        return " 11 11 1111 ";
    }

    public static String generateOneNumWithSpace() {
        Faker faker = new Faker(new Locale("ru"));
        int num = faker.random().nextInt(1, 9);
        return " " + num;
    }

    public static String getSpecialCharacter() {
        Random rand = new Random();
        List<String> list = Arrays.asList("~", "`", "@", "!", "#", "$", "%", "^", "&", "*", "(", ")", "/", "+",
                "№", ";", ":", "?", "<", ">", "{", "}");
        int randomIndex = rand.nextInt(list.size());
        String randomElement = list.get(randomIndex);
        return randomElement;
    }

    public static String generateValidCardNumber() {
        return "4444444444444441";
    }

    public static String generateNoFullCardNumber() {
        Faker faker = new Faker(new Locale("ru"));
        return "111111111111222";
    }

    public static String generateValidOwner() {
        Faker faker = new Faker(new Locale("en-AU"));
        return faker.name().firstName();
    }

    public static String generateValidCardMonth() {
        Faker faker = new Faker(new Locale("ru"));
        Integer monthNow = LocalDate.now().getMonthValue();
        int month = faker.random().nextInt(monthNow, 12);

        if (month < 10) {
            return "0" + month;
        }
        return Integer.toString(month);
    }

    public static String generateValidCardYear() {
        Faker faker = new Faker(new Locale("ru"));
        Integer yearNow = LocalDate.now().getYear() % 100;
        int year = faker.random().nextInt(yearNow, 99);

        if (year <= 9) {
            return "0" + year;
        }
        return Integer.toString(year);
    }

    public static String generateYearNow() {
        Integer yearNow = LocalDate.now().getYear() % 100;
        if (yearNow <= 9) {
            return "0" + yearNow;
        }
        return Integer.toString(yearNow);
    }

    public static String generateMonthNow() {
        Integer monthNow = LocalDate.now().getMonthValue();
        if (monthNow <= 9) {
            return "0" + monthNow;
        }
        return Integer.toString(monthNow);
    }

    public static String generateValidCardCvcCode() {
        Faker faker = new Faker(new Locale("ru"));
        int cvc = faker.random().nextInt(0, 999);

        if (cvc < 10) {
            return "00" + cvc;
        }
        if (cvc >= 10 && cvc < 100) {
            return "0" + cvc;
        }
        return Integer.toString(cvc);
    }

    public static String generateOneNum() {
        Faker faker = new Faker(new Locale("ru"));
        int month = faker.random().nextInt(1, 9);
        return Integer.toString(month);
    }

    public static String generateTwoNum() {
        Faker faker = new Faker(new Locale("ru"));
        int month = faker.random().nextInt(10, 99);
        return Integer.toString(month);
    }

    public static String generateThreeNum() {
        Faker faker = new Faker(new Locale("ru"));
        int month = faker.random().nextInt(100, 999);
        return Integer.toString(month);
    }

    public static String generateInvalidCardMonthUpperMax() {
        Faker faker = new Faker(new Locale("ru"));
        int month = faker.random().nextInt(13, 99);
        return Integer.toString(month);
    }

    public static void cleanData() {
        val runner = new QueryRunner();
        val creditPayment = "DELETE FROM credit_request_entity";
        val order = "DELETE FROM order_entity";
        val debitPayment = "DELETE FROM payment_entity";

        try {
            val connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/app", "app", "pass");
            runner.update(connection, creditPayment);
            runner.update(connection, order);
            runner.update(connection, debitPayment);

        } catch (SQLException ex) {
            System.out.println("SQLException message:" + ex.getMessage());
        }
    }

    public static String searchOperationStatusByDebitCard() {
        val selectStatus = "SELECT status FROM payment_entity WHERE id is not null;";
        try {
            val connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/app", "app", "pass");
            val countStmt = connection.createStatement();
            val result = countStmt.executeQuery(selectStatus);

            if (result.next()) {
                val status = result.getString("status");
                return status;
            }
        } catch (SQLException ex) {
            System.out.println("SQLException message:" + ex.getMessage());
        }
        return null;
    }

    public static String searchOperationStatusByCreditCard() {
        val selectStatus = "SELECT status FROM credit_request_entity WHERE id is not null;";
        try {
            val connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/app", "app", "pass");
            val countStmt = connection.createStatement();
            val result = countStmt.executeQuery(selectStatus);

            if (result.next()) {
                val status = result.getString("status");
                return status;
            }
        } catch (SQLException ex) {
            System.out.println("SQLException message:" + ex.getMessage());
        }
        return null;
    }

    public static String searchOperationByOrderTableForDebitCard() {
        val selectPaymentId = "SELECT payment_id FROM order_entity WHERE id is not null;";
        try {
            val connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/app", "app", "pass");
            val countStmt = connection.createStatement();
            val result = countStmt.executeQuery(selectPaymentId);

            if (result.next()) {
                val paymentId = result.getString("payment_id");
                return paymentId;
            }
        } catch (SQLException ex) {
            System.out.println("SQLException message:" + ex.getMessage());
        }
        return null;
    }

    public static String searchOperationByOrderTableForCreditCard() {
        val selectCreditId = "SELECT credit_id FROM order_entity WHERE id is not null;";
        try {
            val connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/app", "app", "pass");
            val countStmt = connection.createStatement();
            val result = countStmt.executeQuery(selectCreditId);

            if (result.next()) {
                val creditId = result.getString("credit_id");
                return creditId;
            }
        } catch (SQLException ex) {
            System.out.println("SQLException message:" + ex.getMessage());
        }
        return null;
    }

}
