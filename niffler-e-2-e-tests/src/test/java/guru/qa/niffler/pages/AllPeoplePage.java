package guru.qa.niffler.pages;

import com.codeborne.selenide.As;
import com.codeborne.selenide.ElementsCollection;
import org.openqa.selenium.support.FindBy;

public class AllPeoplePage extends BasePage{

    @As("Список значений столбца Actions")
    @FindBy(css = "[class=abstract-table__buttons]")
    private ElementsCollection actionsList;

    public ElementsCollection getActionsList() {
        return actionsList;
    }
}
