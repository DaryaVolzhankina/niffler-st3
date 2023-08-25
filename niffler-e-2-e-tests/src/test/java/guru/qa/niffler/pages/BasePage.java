package guru.qa.niffler.pages;

import guru.qa.niffler.pages.elements.Header;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;

import static com.codeborne.selenide.Selenide.page;

@Slf4j
public abstract class BasePage {
    @Step("Перейти в header")
    public Header getHeader() {
        return page(Header.class);
    }
}
