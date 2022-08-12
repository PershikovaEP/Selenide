package ru.netology;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static com.codeborne.selenide.Selenide.*;

public class CardDeliveryTest {

//Требования к содержимому полей:
//    Город - один из административных центров субъектов РФ
//    Дата - не ранее трёх дней с текущей даты
//    Поле Фамилия и имя - разрешены только русские буквы, дефисы и пробелы
//    Поле телефон - только цифры (11 цифр), символ + (на первом месте)
//    Флажок согласия должен быть выставлен
//    Тестируемая функциональность: отправка формы.

    String date = LocalDate.now().plusDays(7).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
        
    }

    @Test
    void shouldOrderCardDeliveryWhenValuesOfFieldsAreEntered() {
        $x("//*[@data-test-id='city']//input").setValue("Воронеж");
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] input").doubleClick().setValue("19.08.2022");
        $("[data-test-id='name'] input").setValue("Иванов Иван");
        $x("//*[@data-test-id='phone']//input").setValue("+79998889988");
        $("[data-test-id='agreement']").click();
        $x("//*[text()='Забронировать']").click();
        $("[data-test-id='notification'] .notification__content")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.exactText("Встреча успешно забронирована на 19.08.2022"));
    }

    @Test
    void shouldOrderCardDeliveryWhenValueOfFieldCityAreSelectedAndDateCalculate() {
        $x("//*[@data-test-id='city']//input").setValue("Во");
        $x("//*[text()='Воронеж']").click();
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] input").doubleClick().setValue(date);
        $("[data-test-id='name'] input").setValue("Иванов Иван");
        $x("//*[@data-test-id='phone']//input").setValue("+79998889988");
        $("[data-test-id='agreement']").click();
        $x("//*[text()='Забронировать']").click();
        $("[data-test-id='notification'] .notification__content")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.exactText("Встреча успешно забронирована на " + date));
    }

    @Test
    void shouldOrderCardDeliveryWhenValuesOfCityWhenCityWithoutCase() {
        $x("//*[@data-test-id='city']//input").setValue("воронеж");
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] input").doubleClick().setValue(date);
        $("[data-test-id='name'] input").setValue("Иванов Иван");
        $x("//*[@data-test-id='phone']//input").setValue("+79998889988");
        $("[data-test-id='agreement']").click();
        $x("//*[text()='Забронировать']").click();
        $("[data-test-id='notification'] .notification__content")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.exactText("Встреча успешно забронирована на " + date));
    }

    @Test
    void shouldErrorWhenValuesOfCityInLatin() {
        $x("//*[@data-test-id='city']//input").setValue("voronej");
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] input").doubleClick().setValue(date);
        $("[data-test-id='name'] input").setValue("Иванов Иван");
        $x("//*[@data-test-id='phone']//input").setValue("+79998889988");
        $("[data-test-id='agreement']").click();
        $x("//*[text()='Забронировать']").click();
        $("[data-test-id='city'] .input__sub")
                .shouldHave(Condition.exactText("Доставка в выбранный город недоступна"));
    }

    @Test
    void shouldErrorWhenValuesOfCityWhenCityNotAdministrativeCenter() {
        $x("//*[@data-test-id='city']//input").setValue("Острогожск");
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] input").doubleClick().setValue(date);
        $("[data-test-id='name'] input").setValue("Иванов Иван");
        $x("//*[@data-test-id='phone']//input").setValue("+79998889988");
        $("[data-test-id='agreement']").click();
        $x("//*[text()='Забронировать']").click();
        $("[data-test-id='city'] .input__sub")
                .shouldHave(Condition.exactText("Доставка в выбранный город недоступна"));
    }

    @Test
    void shouldOrderCardDeliveryInTheNameWhenWithoutCase() {
        $x("//*[@data-test-id='city']//input").setValue("Во");
        $x("//*[text()='Воронеж']").click();
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] input").doubleClick().setValue(date);
        $("[data-test-id='name'] input").setValue("иванов ИВАН");
        $x("//*[@data-test-id='phone']//input").setValue("+79998889988");
        $("[data-test-id='agreement']").click();
        $x("//*[text()='Забронировать']").click();
        $("[data-test-id='notification'] .notification__content")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.exactText("Встреча успешно забронирована на " + date));
    }


    @Test
    void shouldOrderCardDeliveryInTheNameWihtRussionYo() {
        $x("//*[@data-test-id='city']//input").setValue("Во");
        $x("//*[text()='Воронеж']").click();
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] input").doubleClick().setValue(date);
        $("[data-test-id='name'] input").setValue("Иванов Фёдор");
        $x("//*[@data-test-id='phone']//input").setValue("+79998889988");
        $("[data-test-id='agreement']").click();
        $x("//*[text()='Забронировать']").click();
        $("[data-test-id='notification'] .notification__content")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.exactText("Встреча успешно забронирована на " + date));
    }

    // ограничение длины поля фамилия и имя не установлено
    @Test
    void shouldOrderCardDeliveryWhenNameIs1CharLength() {
        $x("//*[@data-test-id='city']//input").setValue("Во");
        $x("//*[text()='Воронеж']").click();
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] input").doubleClick().setValue(date);
        $("[data-test-id='name'] input").setValue("И");
        $x("//*[@data-test-id='phone']//input").setValue("+79998889988");
        $("[data-test-id='agreement']").click();
        $x("//*[text()='Забронировать']").click();
        $("[data-test-id='notification'] .notification__content")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.exactText("Встреча успешно забронирована на " + date));
    }


    @Test
    void shouldErrorInTheNameInLatin() {
        $x("//*[@data-test-id='city']//input").setValue("Воронеж");
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] input").doubleClick().setValue(date);
        $("[data-test-id='name'] input").setValue("ivanov ivan");
        $x("//*[@data-test-id='phone']//input").setValue("+79998889988");
        $("[data-test-id='agreement']").click();
        $x("//*[text()='Забронировать']").click();
        $("[data-test-id='name'] .input__sub")
                .shouldHave(Condition.exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }


    @Test
    void shouldErrorInTheNameWithSymbol() {
        $x("//*[@data-test-id='city']//input").setValue("Воронеж");
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] input").doubleClick().setValue(date);
        $("[data-test-id='name'] input").setValue("иванов?");
        $x("//*[@data-test-id='phone']//input").setValue("+79998889988");
        $("[data-test-id='agreement']").click();
        $x("//*[text()='Забронировать']").click();
        $("[data-test-id='name'] .input__sub")
                .shouldHave(Condition.exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    void shouldErrorInTheCityIsNotFilled() {
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] input").doubleClick().setValue(date);
        $("[data-test-id='name'] input").setValue("Иванов Иван");
        $x("//*[@data-test-id='phone']//input").setValue("+79998889988");
        $("[data-test-id='agreement']").click();
        $x("//*[text()='Забронировать']").click();
        $("[data-test-id='city'] .input__sub")
                .shouldHave(Condition.exactText("Поле обязательно для заполнения"));
    }

    @Test
    void shouldErrorInThePhoneIsNotFilled() {
        $x("//*[@data-test-id='city']//input").setValue("Воронеж");
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] input").doubleClick().setValue(date);
        $("[data-test-id='name'] input").setValue("Иванов Иван");
        $("[data-test-id='agreement']").click();
        $x("//*[text()='Забронировать']").click();
        $("[data-test-id='phone'] .input__sub")
                .shouldHave(Condition.exactText("Поле обязательно для заполнения"));
    }

    @Test
    void shouldErrorInTheNameIsNotFilled() {
        $x("//*[@data-test-id='city']//input").setValue("Воронеж");
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] input").doubleClick().setValue(date);
        $x("//*[@data-test-id='phone']//input").setValue("+79998889988");
        $("[data-test-id='agreement']").click();
        $x("//*[text()='Забронировать']").click();
        $("[data-test-id='name'] .input__sub")
                .shouldHave(Condition.exactText("Поле обязательно для заполнения"));
    }

    @Test
    void shouldErrorInThePhoneFilledIn10Digits() {
        $x("//*[@data-test-id='city']//input").setValue("Воронеж");
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] input").doubleClick().setValue(date);
        $("[data-test-id='name'] input").setValue("иванов иван");
        $x("//*[@data-test-id='phone']//input").setValue("+7999888998");
        $("[data-test-id='agreement']").click();
        $x("//*[text()='Забронировать']").click();
        $("[data-test-id='phone'] .input__sub")
                .shouldHave(Condition.exactText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    void shouldErrorInThePhoneFilledIn12Digits() {
        $x("//*[@data-test-id='city']//input").setValue("Воронеж");
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] input").doubleClick().setValue(date);
        $("[data-test-id='name'] input").setValue("иванов иван");
        $x("//*[@data-test-id='phone']//input").setValue("+799988899889");
        $("[data-test-id='agreement']").click();
        $x("//*[text()='Забронировать']").click();
        $("[data-test-id='phone'] .input__sub")
                .shouldHave(Condition.exactText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    void shouldErrorInThePhoneFilledInRussiaLetter() {
        $x("//*[@data-test-id='city']//input").setValue("Воронеж");
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] input").doubleClick().setValue(date);
        $("[data-test-id='name'] input").setValue("иванов иван");
        $x("//*[@data-test-id='phone']//input").setValue("+7999888998о");
        $("[data-test-id='agreement']").click();
        $x("//*[text()='Забронировать']").click();
        $("[data-test-id='phone'] .input__sub")
                .shouldHave(Condition.exactText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    void shouldErrorInThePhoneFilledInLatin() {
        $x("//*[@data-test-id='city']//input").setValue("Воронеж");
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] input").doubleClick().setValue(date);
        $("[data-test-id='name'] input").setValue("иванов иван");
        $x("//*[@data-test-id='phone']//input").setValue("+7999888998s");
        $("[data-test-id='agreement']").click();
        $x("//*[text()='Забронировать']").click();
        $("[data-test-id='phone'] .input__sub")
                .shouldHave(Condition.exactText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    void shouldErrorInThePhoneFilledIsNotPlus() {
        $x("//*[@data-test-id='city']//input").setValue("Воронеж");
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] input").doubleClick().setValue(date);
        $("[data-test-id='name'] input").setValue("иванов иван");
        $x("//*[@data-test-id='phone']//input").setValue("79998889988");
        $("[data-test-id='agreement']").click();
        $x("//*[text()='Забронировать']").click();
        $("[data-test-id='phone'] .input__sub")
                .shouldHave(Condition.exactText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    void shouldErrorInThePhoneFilledWhenPlusInMiddle() {
        $x("//*[@data-test-id='city']//input").setValue("Воронеж");
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] input").doubleClick().setValue(date);
        $("[data-test-id='name'] input").setValue("иванов иван");
        $x("//*[@data-test-id='phone']//input").setValue("7+9998889988");
        $("[data-test-id='agreement']").click();
        $x("//*[text()='Забронировать']").click();
        $("[data-test-id='phone'] .input__sub")
                .shouldHave(Condition.exactText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    void shouldErrorWhenFieldsAllIsNotFilled() {
        $x("//*[text()='Забронировать']").click();
        $("[data-test-id='city'] .input__sub")
                .shouldHave(Condition.exactText("Поле обязательно для заполнения"));
    }

    @Test
    void shouldBeVisibleCheckboxClassInputInvalidWhenSendingAnEmptyCheckbox() {
        $x("//*[@data-test-id='city']//input").setValue("Воронеж");
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] input").doubleClick().setValue(date);
        $("[data-test-id='name'] input").setValue("иванов иван");
        $x("//*[@data-test-id='phone']//input").setValue("+79998889988");
        $x("//*[text()='Забронировать']").click();
        $("[data-test-id='agreement'].input_invalid")
                .shouldBe(Condition.visible);
    }

    @Test
    void shouldNotCardDeliveryWhenDeliveryLessThan3Days() {
        String date1 = LocalDate.now().plusDays(2).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        $x("//*[@data-test-id='city']//input").setValue("Во");
        $x("//*[text()='Воронеж']").click();
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] input").doubleClick().setValue(date1);
        $("[data-test-id='name'] input").setValue("Иванов Иван");
        $x("//*[@data-test-id='phone']//input").setValue("+79998889988");
        $("[data-test-id='agreement']").click();
        $x("//*[text()='Забронировать']").click();
        $("[data-test-id='date'] .input__sub")
                .shouldHave(Condition.exactText("Заказ на выбранную дату невозможен"));
    }
}
