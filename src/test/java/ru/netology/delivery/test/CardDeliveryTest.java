package ru.netology.delivery.test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import org.openqa.selenium.Keys;
import ru.netology.delivery.data.DataGenerator;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.*;

class DeliveryTest {

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successful plan and replan meeting")
    void shouldSuccessfulPlanAndReplanMeeting() throws InterruptedException {

        Configuration.holdBrowserOpen = true;

        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        var daysToAddForSecondMeeting = 7;
        var secondMeetingDate = DataGenerator.generateDate(daysToAddForSecondMeeting);

        $x("//input[@placeholder='Город']").setValue(validUser.getCity());
        $x("//input[@placeholder='Дата встречи']").sendKeys(Keys.CONTROL, "a");
        $x("//input[@placeholder='Дата встречи']").sendKeys(Keys.BACK_SPACE);
        $x("//input[@placeholder='Дата встречи']").setValue(firstMeetingDate);
        $("[name=\"name\"]").setValue(validUser.getName());
        $("[name=\"phone\"]").setValue(validUser.getPhone());
        $("[data-test-id = \"agreement\"]").click();
        $x("//span[contains(text(), 'Запланировать')]").click();
        $("[data-test-id=\"success-notification\"]").shouldHave(Condition.text("Встреча успешно " + "запланирована на " + firstMeetingDate), Duration.ofSeconds(15));

        $x("//input[@placeholder='Дата встречи']").sendKeys(Keys.CONTROL, "a");
        $x("//input[@placeholder='Дата встречи']").sendKeys(Keys.BACK_SPACE);
        $x("//input[@placeholder='Дата встречи']").setValue(secondMeetingDate);
        $x("//span[contains(text(), 'Запланировать')]").click();
        $("[data-test-id=\"replan-notification\"]").shouldHave(Condition.text("У вас уже запланирована " + "встреча на другую дату."));
        $x("//span[contains(text(), 'Перепланировать')]").click();
        $("[data-test-id=\"success-notification\"]").shouldHave(Condition.text("Встреча успешно" + " запланирована на " + secondMeetingDate), Duration.ofSeconds(15));

    }
}