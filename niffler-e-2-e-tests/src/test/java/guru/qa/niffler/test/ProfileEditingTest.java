package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import com.github.javafaker.Faker;
import guru.qa.niffler.db.model.auth.AuthUserEntity;
import guru.qa.niffler.jupiter.annotation.DBUser;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;

public class ProfileEditingTest extends BaseWebTest{

    private final Faker faker = new Faker();

    @BeforeEach
    void doLogin(AuthUserEntity user) {
        Selenide.open("http://127.0.0.1:3000/main");
        pages.mainPage()
                .getLoginBtn()
                .click();
        pages.loginPage()
                .getUsernameField()
                .setValue(user.getUsername());
        pages.loginPage()
                .getPasswordField()
                .setValue(user.getPassword());
        pages.loginPage()
                .getSignInBtn()
                .click();
        pages.homePage()
                .getStatisticsGraph()
                .shouldBe(visible);
    }

    @DBUser
    @Test
    @AllureId("10")
    void changeNameSurnameTest() {
        pages.homePage()
                .getHeader()
                .getProfileIcon()
                .click();
        pages.profilePage()
                .getFirstnameInput()
                .setValue(faker.name().firstName());
        pages.profilePage()
                .getSurnameInput()
                .setValue(faker.name().lastName());
        pages.profilePage()
                .getSubmitBtn()
                .click();
        pages.profilePage()
                .getNotificationContainer()
                .shouldBe(visible)
                .shouldHave(text("Profile updated!"));
    }
}
