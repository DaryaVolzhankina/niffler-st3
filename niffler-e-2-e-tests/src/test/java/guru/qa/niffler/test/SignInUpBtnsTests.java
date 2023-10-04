package guru.qa.niffler.test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.webdriver;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SignInUpBtnsTests extends BaseWebTest {

    @Test
    @AllureId("8")
    void signInBtnTest() {
        Selenide.open("http://127.0.0.1:9000/register");

        pages.registrationPage()
                .getSignInBtn()
                .click();

        pages.loginPage()
                .getSignUpBtn()
                .shouldBe(Condition.visible);

        assertEquals(webdriver().driver().url(), "http://127.0.0.1:9000/login");
    }

    @Test
    @AllureId("9")
    void signUpBtnTest() {
        Selenide.open("http://127.0.0.1:9000/login");

        pages.loginPage()
                .getSignUpBtn()
                .click();

        pages.registrationPage()
                .getSignInBtn()
                .shouldBe(Condition.visible);

        assertEquals(webdriver().driver().url(), "http://127.0.0.1:9000/register");
    }
}
