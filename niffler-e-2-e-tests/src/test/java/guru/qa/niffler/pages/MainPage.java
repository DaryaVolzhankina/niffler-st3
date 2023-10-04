package guru.qa.niffler.pages;

import com.codeborne.selenide.As;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.support.FindBy;

public class MainPage extends BasePage{
    @As("Кнопка Логин")
    @FindBy(css = "a[href*='redirect']")
    private SelenideElement loginBtn;

    @As("Кнопка Регистрация")
    @FindBy(css = "a[href*='register']")
    private SelenideElement registerBtn;

    public SelenideElement getLoginBtn() {
        return loginBtn;
    }

    public SelenideElement getRegisterBtn() {
        return registerBtn;
    }
}
