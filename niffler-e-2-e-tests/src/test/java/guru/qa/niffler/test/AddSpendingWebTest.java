package guru.qa.niffler.test;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import guru.qa.niffler.db.model.auth.AuthUserEntity;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.DBUser;
import guru.qa.niffler.jupiter.annotation.DeleteSpendings;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;

public class AddSpendingWebTest extends BaseWebTest {

    private static final String CATEGORY = "Красота";
    private static final double AMOUNT = 14000.00;

    static {
        Configuration.browser = "chrome";
        Configuration.browserSize = "1980x1024";
    }

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
    @Category(
            category = CATEGORY
    )
    @DeleteSpendings()
    @Test
    @AllureId("7")
    void addSpendingWebTest() {
        pages.homePage()
                .getCategoryInput()
                .setValue(CATEGORY + "\n");
        pages.homePage()
                .getAmountInput()
                .setValue(String.valueOf(AMOUNT));
        pages.homePage()
                .getSubmitBtn()
                .click();
        pages.homePage()
                .getSpendingsTable()
                .scrollIntoView(true)
                .$$("tr")
                .shouldHave(size(1))
                .find(text(CATEGORY))
                .shouldBe(visible);
    }
}
