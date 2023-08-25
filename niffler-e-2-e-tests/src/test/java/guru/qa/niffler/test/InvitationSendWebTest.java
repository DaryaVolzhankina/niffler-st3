package guru.qa.niffler.test;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.UserJson;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.$;
import static guru.qa.niffler.jupiter.annotation.User.UserType.INVITATION_SENT;

public class InvitationSendWebTest extends BaseWebTest {

    @BeforeEach
    void doLogin(@User(userType = INVITATION_SENT) UserJson userForTest) {
        Selenide.open("http://127.0.0.1:3000/main");
        $("a[href*='redirect']").click();
        $("input[name='username']").setValue(userForTest.getUsername());
        $("input[name='password']").setValue(userForTest.getPassword());
        $("button[type='submit']").click();
    }

    @Test
    @AllureId("104")
    void pendingInvitationShouldBeDisplayedInTable0() throws InterruptedException {
        pages.mainPage()
                .getHeader()
                .getAllPeopleIcon()
                .click();
        pages.allPeoplePage()
                .getActionsList()
                .filterBy(Condition.text("Pending invitation"))
                .shouldHave(CollectionCondition.sizeGreaterThanOrEqual(1));
    }

    @Test
    @AllureId("105")
    void pendingInvitationShouldBeDisplayedInTable1() throws InterruptedException {
        pages.mainPage()
                .getHeader()
                .getAllPeopleIcon()
                .click();
        pages.allPeoplePage()
                .getActionsList()
                .filterBy(Condition.text("Pending invitation"))
                .shouldHave(CollectionCondition.sizeGreaterThanOrEqual(1));
    }

    @Test
    @AllureId("106")
    void pendingInvitationShouldBeDisplayedInTable2() throws InterruptedException {
        pages.mainPage()
                .getHeader()
                .getAllPeopleIcon()
                .click();
        pages.allPeoplePage()
                .getActionsList()
                .filterBy(Condition.text("Pending invitation"))
                .shouldHave(CollectionCondition.sizeGreaterThanOrEqual(1));
    }
}
