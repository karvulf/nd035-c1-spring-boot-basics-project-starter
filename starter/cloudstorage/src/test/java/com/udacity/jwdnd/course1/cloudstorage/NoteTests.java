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
class NoteTests {

    @LocalServerPort
    private int port;

    private WebDriver driver;
    private SignUpPage signUpPage;
    private LoginPage loginPage;
    private HomePage homePage;

    private String noteTitle = "note_title";
    private String noteDescription = "note_description";

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

        signUpAndGoToNoteTab();
    }

    @AfterEach
    public void afterEach() {
        if (this.driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testCreateNote() throws InterruptedException {
        createNewNote(noteTitle, noteDescription);

        WebElement titleElement = driver.findElement(By.xpath(".//*[text()='" + noteTitle + "']"));
        assertNotNull(titleElement);
        WebElement noteDescriptionElement = driver.findElement(By.xpath(".//*[text()='" + noteDescription + "']"));
        assertNotNull(noteDescriptionElement);
    }

    @Test
    public void testEditNote() throws InterruptedException {
        createNewNote(noteTitle, noteDescription);

        // click on edit button
        homePage.edit();
        Thread.sleep(500);

        // check if title and description are unchanged
        assertEquals(noteTitle, homePage.getNoteTitleModal());
        assertEquals(noteDescription, homePage.getNoteDescriptionModal());

        // edit note title and description
        String newNoteTitle = "edited_note_title";
        String newNoteDescription = "edited_note_description";
        homePage.clearInputNoteModal();
        homePage.createOrUpdateNote(newNoteTitle, newNoteDescription);
        Thread.sleep(500);

        // check for changes
        homePage.navigateToNoteTab();
        Thread.sleep(500);
        WebElement titleElement = driver.findElement(By.xpath(".//*[text()='" + newNoteTitle + "']"));
        assertNotNull(titleElement);
        WebElement descriptionElement = driver.findElement(By.xpath(".//*[text()='" + newNoteDescription + "']"));
        assertNotNull(descriptionElement);
    }

    @Test
    public void testDeleteNote() throws InterruptedException {
        createNewNote(noteTitle, noteDescription);

        // delete note
        homePage.delete();
        Thread.sleep(500);
        homePage.navigateToNoteTab();
        Thread.sleep(500);

        // check if note is deleted
        assertThrows(NoSuchElementException.class, () -> {
            driver.findElement(By.xpath(".//*[text()='" + noteTitle + "']"));
        }) ;
        // check if note is deleted
        assertThrows(NoSuchElementException.class, () -> {
            driver.findElement(By.xpath(".//*[text()='" + noteDescription + "']"));
        }) ;
    }

    private void createNewNote(String title, String description) throws InterruptedException {
        // open note modal
        homePage.openNoteModal();
        Thread.sleep(1000);

        // create note
        homePage.createOrUpdateNote(title, description);
        Thread.sleep(500);

        // check for new note
        homePage.navigateToNoteTab();
        Thread.sleep(500);
    }

    private void signUpAndGoToNoteTab() throws InterruptedException {
        driver.get(getSignUpUrl());
        User user = getNewRandomUser();
        signUpPage.signup(user.getUsername(), user.getPassword(), user.getFirstName(), user.getLastName());

        driver.get(getLoginUrl());
        loginPage.login(user.getUsername(), user.getPassword());
        Thread.sleep(500);

        // navigate to tab notes
        homePage.navigateToNoteTab();
        Thread.sleep(500);
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

    private User getNewRandomUser() {
        byte[] array = new byte[7]; // length is bounded by 7
        new Random().nextBytes(array);
        String generatedString = new String(array, Charset.forName("UTF-8"));
        return new User(null, generatedString, null,
                "password", "firstname", "lastname");
    }
}
