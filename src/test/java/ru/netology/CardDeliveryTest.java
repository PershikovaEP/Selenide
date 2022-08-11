package ru.netology;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
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


    @Test
    void shouldOrderCardDeliveryWhenValuesOfFieldsAreEntered() {
        open("http://localhost:9999");
        $x("//*[@data-test-id='city']//input").setValue("Воронеж");
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] input").doubleClick().setValue("19.08.2022");
        $("[data-test-id='name'] input").setValue("Иванов Иван");
        $x("//*[@data-test-id='phone']//input").setValue("+79998889988");
        $("[data-test-id='agreement']").click();
        $x("//*[text()='Забронировать']").click();
        $("[data-test-id='notification'] .notification__content")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldBe(Condition.exactText("Встреча успешно забронирована на 19.08.2022"));
    }

    @Test
    void shouldOrderCardDeliveryWhenValueOfFieldCityAreSelectedAndDateCalculate() {
        String date = LocalDate.now().plusDays(7).format(DateTimeFormatter.ofPattern("dd.MM.yyyy")); //сокращенная надпись
        open("http://localhost:9999");
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
                .shouldBe(Condition.exactText("Встреча успешно забронирована на " + date));
    }
}
