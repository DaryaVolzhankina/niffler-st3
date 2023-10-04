package guru.qa.niffler.pages;

import com.codeborne.selenide.As;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.support.FindBy;

public class HomePage extends BasePage{

    @As("График статистики")
    @FindBy(css = ".main-content__section-stats")
    private SelenideElement statisticsGraph;

    @As("Поле Категория")
    @FindBy(id = "react-select-3-input")
    private SelenideElement categoryInput;

    @As("Поле Сумма")
    @FindBy(css = "[placeholder='Set Amount']")
    private SelenideElement amountInput;

    @As("Поле Дата")
    @FindBy(css = "div[class='react-datepicker__input-container']>input[type=text]")
    private SelenideElement datePicker;

    @As("Кнопка Добавить новую трату")
    @FindBy(css = "button[type=submit]")
    private SelenideElement submitBtn;

    @As("Тело таблицы трат")
    @FindBy(css = ".spendings__content tbody")
    private SelenideElement spendingsTable;

    public SelenideElement getStatisticsGraph() {
        return statisticsGraph;
    }

    public SelenideElement getCategoryInput() {
        return categoryInput;
    }

    public SelenideElement getAmountInput() {
        return amountInput;
    }

    public SelenideElement getSubmitBtn() {
        return submitBtn;
    }

    public SelenideElement getDatePicker() {
        return datePicker;
    }

    public SelenideElement getSpendingsTable() {
        return spendingsTable;
    }
}
