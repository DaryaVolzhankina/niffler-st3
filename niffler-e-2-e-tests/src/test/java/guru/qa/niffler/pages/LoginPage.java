package guru.qa.niffler.pages;

import com.codeborne.selenide.As;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.support.FindBy;

public class LoginPage extends BasePage{

    @As("Поле Username")
    @FindBy(css = "input[name='username']")
    private SelenideElement usernameField;

    @As("Поле Password")
    @FindBy(css = "input[name='password']")
    private SelenideElement passwordField;

    @As("Кнопка Sign In")
    @FindBy(css = "button[type='submit']")
    private SelenideElement signInBtn;

    @As("Кнопка Регистрация")
    @FindBy(css = "a[href*='register']")
    private SelenideElement signUpBtn;

    public SelenideElement getUsernameField() {
        return usernameField;
    }

    public SelenideElement getPasswordField() {
        return passwordField;
    }

    public SelenideElement getSignInBtn() {
        return signInBtn;
    }

    public SelenideElement getSignUpBtn() {
        return signUpBtn;
    }
}
