package guru.qa.niffler.pages.elements;

import com.codeborne.selenide.As;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.support.FindBy;

public class Header {

    @As("Иконка All People")
    @FindBy(css = "[alt='Иконка глобуса']")
    private SelenideElement allPeopleIcon;

    @As("Иконка Friends")
    @FindBy(css = "[alt='Иконка друзей']")
    private SelenideElement friendsIcon;

    @As("Иконка Профиль")
    @FindBy(css = "[class='header__avatar']")
    private SelenideElement profileIcon;

    @As("Иконка выхода")
    @FindBy(css = "button[class*=logout]")
    private SelenideElement logoutBtn;

    public SelenideElement getAllPeopleIcon() {
        return allPeopleIcon;
    }
    public SelenideElement getFriendsIcon() {
        return friendsIcon;
    }
    public SelenideElement getLogoutBtn() {
        return logoutBtn;
    }
    public SelenideElement getProfileIcon() {
        return profileIcon;
    }
}
