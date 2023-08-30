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
import static guru.qa.niffler.jupiter.annotation.User.UserType.INVITATION_RECEIVED;

public class InvitationReceivedWebTest extends BaseWebTest {

    @BeforeEach
    void doLogin(@User(userType = INVITATION_RECEIVED) UserJson userForTest) {
        Selenide.open("http://127.0.0.1:3000/main");
        $("a[href*='redirect']").click();
        $("input[name='username']").setValue(userForTest.getUsername());
        $("input[name='password']").setValue(userForTest.getPassword());
        $("button[type='submit']").click();
    }

    @Test
    @AllureId("107")
    void submitInvitationShouldBeDisplayedInTable0() throws InterruptedException {
        pages.mainPage()
                .getHeader()
                .getFriendsIcon()
                .click();
        pages.friendsPage()
                .getNoFriendsField()
                .shouldNotBe(Condition.visible);
        pages.friendsPage()
                .getActionsList()
                .should(CollectionCondition.sizeGreaterThan(0));
        pages.friendsPage()
                .getActionsList()
                .first()
                .shouldBe(Condition.visible);
    }

    @Test
    @AllureId("108")
    void submitInvitationShouldBeDisplayedInTable1() throws InterruptedException {
        pages.mainPage()
                .getHeader()
                .getFriendsIcon()
                .click();
        pages.friendsPage()
                .getNoFriendsField()
                .shouldNotBe(Condition.visible);
        pages.friendsPage()
                .getActionsList()
                .should(CollectionCondition.sizeGreaterThan(0));
        pages.friendsPage()
                .getActionsList()
                .first()
                .shouldBe(Condition.visible);
    }

    @Test
    @AllureId("109")
    void submitInvitationShouldBeDisplayedInTable2() throws InterruptedException {
        pages.mainPage()
                .getHeader()
                .getFriendsIcon()
                .click();
        pages.friendsPage()
                .getNoFriendsField()
                .shouldNotBe(Condition.visible);
        pages.friendsPage()
                .getActionsList()
                .should(CollectionCondition.sizeGreaterThan(0));
        pages.friendsPage()
                .getActionsList()
                .first()
                .shouldBe(Condition.visible);
    }
}
