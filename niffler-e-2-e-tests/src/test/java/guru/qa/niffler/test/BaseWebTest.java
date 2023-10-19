package guru.qa.niffler.test;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.WebTest;
import guru.qa.niffler.pages.Pages;
import org.junit.jupiter.api.BeforeEach;

@WebTest
public abstract class BaseWebTest {
    protected Pages pages;
    protected static final Config CFG = Config.getInstance();

    @BeforeEach
    public void setupTest() {
        pages = new Pages();
    }
}
