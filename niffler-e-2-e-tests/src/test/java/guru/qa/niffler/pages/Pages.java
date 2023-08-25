package guru.qa.niffler.pages;

import static com.codeborne.selenide.Selenide.page;

public class Pages {
    private MainPage mainPage;
    private FriendsPage friendsPage;
    private AllPeoplePage allPeoplePage;

    public <PageObjectClass> PageObjectClass getPage(Class<PageObjectClass> pageObjectClass) {
        return page(pageObjectClass);
    }

    public MainPage mainPage() {
        return mainPage = (mainPage == null ? page(MainPage.class) : mainPage);
    }

    public FriendsPage friendsPage() {
        return friendsPage = (friendsPage == null ? page(FriendsPage.class) : friendsPage);
    }

    public AllPeoplePage allPeoplePage() {
        return allPeoplePage = (allPeoplePage == null ? page(AllPeoplePage.class) : allPeoplePage);
    }
}
