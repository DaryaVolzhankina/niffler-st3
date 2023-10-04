package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.db.model.auth.AuthUserEntity;
import guru.qa.niffler.jupiter.annotation.DBUser;
import guru.qa.niffler.jupiter.extension.DaoExtension;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.codeborne.selenide.Condition.visible;

@ExtendWith(DaoExtension.class)
public class LogoutTest extends BaseWebTest {

    @DBUser
    @Test
    @AllureId("5")
    void loginPageShouldBeVisibleAfterLogoutTest(AuthUserEntity user) {
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

        pages.homePage()
                .getHeader()
                .getLogoutBtn()
                .click();

        pages.mainPage()
                .getLoginBtn()
                .shouldBe(visible);
        pages.mainPage()
                .getRegisterBtn()
                .shouldBe(visible);
    }
}