package guru.qa.niffler.pages;

import com.codeborne.selenide.As;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.support.FindBy;

public class FriendsPage extends BasePage{

    @As("Таблица с друзьями")
    @FindBy(css = "[class='table abstract-table']")
    private SelenideElement friendsTable;

    @As("Надпись There are no friends yet!")
    @FindBy(css = "section>div")
    private SelenideElement noFriendsField;

    @As("Список друзей")
    @FindBy(css = "[class='table abstract-table']>tbody>tr")
    private ElementsCollection friendsList;

    @As("Список значений столбца Actions с запросом в друзья")
    @FindBy(css = "[class='abstract-table__buttons']>[data-tooltip-id=submit-invitation]")
    private ElementsCollection actionsList;

    public ElementsCollection getActionsList() {
        return actionsList;
    }

    public SelenideElement getFriendsTable() {
        return friendsTable;
    }

    public SelenideElement getNoFriendsField() {
        return noFriendsField;
    }

    public ElementsCollection getFriendsList() {
        return friendsList;
    }

}
