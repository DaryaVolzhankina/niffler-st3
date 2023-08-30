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
import static guru.qa.niffler.jupiter.annotation.User.UserType.WITH_FRIENDS;

public class FriendsWebTest extends BaseWebTest {

    @BeforeEach
    void doLogin(@User(userType = WITH_FRIENDS) UserJson userForTest) {
        Selenide.open("http://127.0.0.1:3000/main");
        $("a[href*='redirect']").click();
        $("input[name='username']").setValue(userForTest.getUsername());
        $("input[name='password']").setValue(userForTest.getPassword());
        $("button[type='submit']").click();
    }

    @Test
    @AllureId("101")
    void friendShouldBeDisplayedInTable0(@User(userType = WITH_FRIENDS) UserJson userForTest, @User(userType = INVITATION_RECEIVED) UserJson userForTestAnother) throws InterruptedException {
        System.out.println(userForTest.getUsername());
        System.out.println(userForTestAnother.getUsername());
        pages.mainPage()
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

    @Test
    @AllureId("102")
    void friendShouldBeDisplayedInTable1() throws InterruptedException {
        pages.mainPage()
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

    @Test
    @AllureId("103")
    void friendShouldBeDisplayedInTable2() throws InterruptedException {
        pages.mainPage()
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
