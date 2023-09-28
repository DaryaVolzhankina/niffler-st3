package guru.qa.niffler.pages;

import com.codeborne.selenide.As;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.support.FindBy;

public class ProfilePage extends BasePage{
    @As("Поле Имя")
    @FindBy(css = "[name='firstname']")
    private SelenideElement firstnameInput;

    @As("Поле Фамилия")
    @FindBy(css = "[name='surname']")
    private SelenideElement surnameInput;

    @As("Кнопка Submit")
    @FindBy(css = "button[type='submit']")
    private SelenideElement submitBtn;

    @As("Контейнер уведомления")
    @FindBy(css = "div[class='Toastify__toast-body']")
    private SelenideElement notificationContainer;

    public SelenideElement getFirstnameInput() {
        return firstnameInput;
    }

    public SelenideElement getSurnameInput() {
        return surnameInput;
    }

    public SelenideElement getSubmitBtn() {
        return submitBtn;
    }

    public SelenideElement getNotificationContainer() {
        return notificationContainer;
    }
}
