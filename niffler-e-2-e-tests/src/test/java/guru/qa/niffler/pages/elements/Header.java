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

    public SelenideElement getAllPeopleIcon() {
        return allPeopleIcon;
    }

    public SelenideElement getFriendsIcon() {
        return friendsIcon;
    }
}
