package guru.qa.niffler.pages;

import static com.codeborne.selenide.Selenide.page;

public class Pages {
    private HomePage homePage;
    private MainPage mainPage;
    private FriendsPage friendsPage;
    private AllPeoplePage allPeoplePage;
    private LoginPage loginPage;
    private RegistrationPage registrationPage;
    private ProfilePage profilePage;

    public <PageObjectClass> PageObjectClass getPage(Class<PageObjectClass> pageObjectClass) {
        return page(pageObjectClass);
    }

    public HomePage homePage() {
        return homePage = (homePage == null ? page(HomePage.class) : homePage);
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

    public LoginPage loginPage() {
        return loginPage = (loginPage == null ? page(LoginPage.class) : loginPage);
    }
    public ProfilePage profilePage() {
        return profilePage = (profilePage == null ? page(ProfilePage.class) : profilePage);
    }
    public RegistrationPage registrationPage() {
        return registrationPage = (registrationPage == null ? page(RegistrationPage.class) : registrationPage);
    }
}
