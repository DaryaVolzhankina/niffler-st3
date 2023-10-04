package guru.qa.niffler.pages;

import com.codeborne.selenide.As;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.support.FindBy;

public class RegistrationPage extends BasePage{

    @As("Кнопка Sign In")
    @FindBy(css = "a[href*='redirect']")
    private SelenideElement signInBtn;

    public SelenideElement getSignInBtn() {
        return signInBtn;
    }
}
