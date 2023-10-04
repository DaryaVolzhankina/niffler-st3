package guru.qa.niffler.test;


import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.UserJson;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static guru.qa.niffler.jupiter.annotation.User.UserType.INVITATION_RECEIVED;
import static guru.qa.niffler.jupiter.annotation.User.UserType.WITH_FRIENDS;

public class FriendsWebTest extends BaseWebTest {

    @BeforeEach
    void doLogin(@User(userType = WITH_FRIENDS) UserJson userForTest) {
        Selenide.open("http://127.0.0.1:3000/main");
        pages.mainPage()
                .getLoginBtn()
                .click();
        pages.loginPage()
                .getUsernameField()
                .setValue(userForTest.getUsername());
        pages.loginPage()
                .getPasswordField()
                .setValue(userForTest.getPassword());
        pages.loginPage()
                .getSignInBtn()
                .click();
        pages.homePage()
                .getStatisticsGraph()
                .shouldBe(visible);
    }

    @Test
    @AllureId("1")
    void friendShouldBeDisplayedInTable(@User(userType = WITH_FRIENDS) UserJson userForTest, @User(userType = INVITATION_RECEIVED) UserJson userForTestAnother) throws InterruptedException {
        System.out.println(userForTest.getUsername());
        System.out.println(userForTestAnother.getUsername());
        pages.homePage()
                .getHeader()
                .getFriendsIcon()
                .click();
        pages.friendsPage()
                .getNoFriendsField()
                .shouldNotBe(Condition.visible);
        pages.friendsPage()
                .getFriendsList()
                .should(CollectionCondition.sizeGreaterThan(0));
        pages.friendsPage()
                .getFriendsList()
                .get(0)
                .$$("td")
                .get(3)
                .shouldHave(Condition.text("You are friends"));
    }
}
