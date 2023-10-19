package guru.qa.niffler.test;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import guru.qa.niffler.db.model.auth.AuthUserEntity;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.DBUser;
import guru.qa.niffler.jupiter.annotation.Spend;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class DeleteSpendingWebTest extends BaseWebTest {

    private static final String CATEGORY = "Здоровье";
    private static final String DESCRIPTION = "Аптека";
    private static final double AMOUNT = 14000.00;

    static {
        Configuration.browser = "chrome";
        Configuration.browserSize = "1980x1024";
    }


    @DBUser
    @ApiLogin
    @Category(
            category = CATEGORY
    )
    @Spend(
            description = DESCRIPTION,
            amount = AMOUNT,
            currency = CurrencyValues.RUB
    )
    @Test
    @AllureId("6")
    void spendingShouldBeDeletedAfterDeleteAction(CategoryJson createdCategory, SpendJson createdSpend, AuthUserEntity user) {
        Selenide.open(CFG.nifflerFrontUrl() + "/main");

        $(".spendings__content tbody")
                .$$("tr")
                .find(text(createdSpend.getDescription()))
                .$$("td")
                .first()
                .scrollTo()
                .click();

        Allure.step(
                "Delete spending",
                () -> $(byText("Delete selected")).click()
        );

        Allure.step(
                "Check spendings",
                () -> $(".spendings__content tbody")
                        .$$("tr")
                        .shouldHave(size(0))
        );
    }
}
