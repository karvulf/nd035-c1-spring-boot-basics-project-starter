package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.model.User;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthTests {

    @LocalServerPort
    private int port;

    private WebDriver driver;
    private SignUpPage signUpPage;
    private LoginPage loginPage;
    private HomePage homePage;

    @BeforeAll
    static void beforeAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void beforeEach() {
        this.driver = new ChromeDriver();
        loginPage = new LoginPage(driver);
        signUpPage = new SignUpPage(driver);
        homePage = new HomePage(driver);
    }

    @AfterEach
    public void afterEach() {
        if (this.driver != null) {
            driver.quit();
        }
    }

    @Test
    public void getLoginPage() {
        driver.get("http://localhost:" + this.port + "/login");
        Assertions.assertEquals("Login", driver.getTitle());
    }

    @Test
    public void testUnauthorizedUser() {
        // check signup page
        String signUpUrl = getSignUpUrl();
        driver.get(signUpUrl);
        assertEquals(driver.getCurrentUrl(), signUpUrl);

        // check login page
        String loginUrl = getLoginUrl();
        driver.get(loginUrl);
        assertEquals(driver.getCurrentUrl(), loginUrl);

        // check home page
        String homeUrl = getHomeUrl();
        driver.get(homeUrl);
        assertEquals(driver.getCurrentUrl(), loginUrl);
    }

    @Test
    public void testAuthorizedUser() {
        // go to signup
        driver.get(getLoginUrl());
        loginPage.navigateToSignup();
        assertEquals(driver.getCurrentUrl(), getSignUpUrl());

        // sign up user
        User user = getNewUser();
        signUpPage.signup(user.getUsername(), user.getPassword(), user.getFirstName(), user.getLastName());
        assertEquals(signUpPage.getSuccessMsg(), "You successfully signed up! Please continue to the login page.");

        // go to login
        signUpPage.navigateToLogin();
        assertEquals(driver.getCurrentUrl(), getLoginUrl());

        // login user
        loginPage.login(user.getUsername(), user.getPassword());
        waitForElement("nav-tab", 1000);

        // check if home is accessible
        assertEquals(driver.getCurrentUrl(), getHomeUrl());
        driver.get(getHomeUrl());
        assertEquals(driver.getCurrentUrl(), getHomeUrl());

        // logout
        homePage.logout();
        waitForElement("signup-link", 1000);
        assertEquals(driver.getCurrentUrl(), getLoginUrl());

        // check if home isn't accessible
        driver.get(getHomeUrl());
        assertEquals(driver.getCurrentUrl(), getLoginUrl());
    }

    private void waitForElement(String id, long time) {
        WebDriverWait wait = new WebDriverWait(driver, time);
        wait.until(webDriver -> webDriver.findElement(By.id(id)));
    }

    private String getSignUpUrl() {
        return getUrl() + "/signup";
    }

    private String getLoginUrl() {
        return getUrl() + "/login";
    }

    private String getHomeUrl() {
        return getUrl() + "/home";
    }

    private String getUrl() {
        return "http://localhost:" + this.port;
    }

    private User getNewUser() {
        return new User(null, "username", null,
                "password", "firstname", "lastname");
    }

}
