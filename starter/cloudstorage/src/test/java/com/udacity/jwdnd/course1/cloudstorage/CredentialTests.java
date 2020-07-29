package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.model.User;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.nio.charset.Charset;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CredentialTests {

    @LocalServerPort
    private int port;

    private WebDriver driver;
    private SignUpPage signUpPage;
    private LoginPage loginPage;
    private HomePage homePage;

    private String credentialUrl = "credential_url";
    private String credentialUsername = "credential_username";
    private String credentialPassword = "credential_password";

    @BeforeAll
    static void beforeAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void beforeEach() throws InterruptedException {
        this.driver = new ChromeDriver();
        loginPage = new LoginPage(driver);
        signUpPage = new SignUpPage(driver);
        homePage = new HomePage(driver);

        signUpAndGoToCredentialTab();
    }

    @AfterEach
    public void afterEach() {
        if (this.driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testCreateCredential() throws InterruptedException {
        createNewCredential(credentialUrl, credentialUsername, credentialPassword);

        // username and password should be visible
        WebElement credentialUrlElement = driver.findElement(By.xpath(".//*[text()='" + credentialUrl + "']"));
        assertNotNull(credentialUrlElement);
        WebElement credentialUsernameElement = driver.findElement(By.xpath(".//*[text()='" + credentialUsername + "']"));
        assertNotNull(credentialUsernameElement);

        // should not be visible with decrypted password
        assertThrows(NoSuchElementException.class, () -> {
            driver.findElement(By.xpath(".//*[text()='" + credentialPassword + "']"));
        });
    }

    @Test
    public void testEditCredential() throws InterruptedException {
        createNewCredential(credentialUrl, credentialUsername, credentialPassword);

        // click on edit button
        homePage.edit();
        Thread.sleep(500);

        // check if url, username and password are unchanged
        assertEquals(credentialUrl, homePage.getCredentialUrlModal());
        assertEquals(credentialUsername, homePage.getCredentialUsernameModal());
        assertEquals(credentialPassword, homePage.getCredentialPasswordModal()); // checks also if password is encrypted


        // edit url, username and password
        String newCredentialUrl = "edited_credential_url";
        String newCredentialUsername = "edited_credential_username";
        String newCredentialPassword = "edited_credential_password";
        homePage.clearInputCredentialModal();
        homePage.createOrUpdateCredential(newCredentialUrl, newCredentialUsername, newCredentialPassword);
        Thread.sleep(500);

        // check for changes
        homePage.navigateToNoteTab();
        Thread.sleep(500);
        WebElement urlElement = driver.findElement(By.xpath(".//*[text()='" + newCredentialUrl + "']"));
        assertNotNull(urlElement);
        WebElement usernameElement = driver.findElement(By.xpath(".//*[text()='" + newCredentialUsername + "']"));
        assertNotNull(usernameElement);
        assertThrows(NoSuchElementException.class, () -> {
            driver.findElement(By.xpath(".//*[text()='" + newCredentialPassword + "']"));
        }) ;
    }

    @Test
    public void testDeleteCredential() throws InterruptedException {
        createNewCredential(credentialUrl, credentialUsername, credentialPassword);

        // delete credential
        homePage.delete();
        Thread.sleep(500);
        homePage.navigateToCredentialsTab();
        Thread.sleep(500);

        // check if credential is deleted
        assertThrows(NoSuchElementException.class, () -> {
            driver.findElement(By.xpath(".//*[text()='" + credentialUrl + "']"));
        }) ;
        assertThrows(NoSuchElementException.class, () -> {
            driver.findElement(By.xpath(".//*[text()='" + credentialUsername + "']"));
        }) ;
    }

    private void createNewCredential(String url, String username, String password) throws InterruptedException {
        // open note modal
        homePage.openCredentialModal();
        Thread.sleep(1000);

        // create note
        homePage.createOrUpdateCredential(url, username, password);
        Thread.sleep(500);

        // check for new note
        homePage.navigateToCredentialsTab();
        Thread.sleep(500);
    }

    private void signUpAndGoToCredentialTab() throws InterruptedException {
        driver.get(getSignUpUrl());
        User user = getNewRandomUser();
        signUpPage.signup(user.getUsername(), user.getPassword(), user.getFirstName(), user.getLastName());

        driver.get(getLoginUrl());
        loginPage.login(user.getUsername(), user.getPassword());
        Thread.sleep(500);

        // navigate to tab notes
        homePage.navigateToCredentialsTab();
        Thread.sleep(500);
    }

    private String getSignUpUrl() {
        return getUrl() + "/signup";
    }

    private String getLoginUrl() {
        return getUrl() + "/login";
    }

    private String getUrl() {
        return "http://localhost:" + this.port;
    }

    private User getNewRandomUser() {
        byte[] array = new byte[7]; // length is bounded by 7
        new Random().nextBytes(array);
        String generatedString = new String(array, Charset.forName("UTF-8"));
        return new User(null, generatedString, null,
                "password", "firstname", "lastname");
    }
}
