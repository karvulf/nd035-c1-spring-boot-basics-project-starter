package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class HomePage {

    @FindBy(id = "nav-tab")
    private WebElement navTab;

    @FindBy(id = "logout-button")
    private WebElement logoutButton;

    @FindBy(id = "nav-notes-tab")
    private WebElement noteTab;

    @FindBy(id = "nav-credentials-tab")
    private WebElement credentialsTab;

    @FindBy(id = "add-new-note-button")
    private WebElement addNewNoteButton;

    @FindBy(id = "add-new-credential-button")
    private WebElement addNewCredentialButton;

    @FindBy(id = "note-title")
    private WebElement noteTitleInput;

    @FindBy(id = "note-description")
    private WebElement noteDescriptionInput;

    @FindBy(xpath = ".//*[text()='Edit']")
    private WebElement editButton;

    @FindBy(xpath = ".//*[text()='Delete']")
    private WebElement deleteButton;

    @FindBy(id = "credential-url")
    private WebElement credentialUrlInput;

    @FindBy(id = "credential-username")
    private WebElement credentialUsernameInput;

    @FindBy(id = "credential-password")
    private WebElement credentialPasswordInput;

    public HomePage(WebDriver driver){
        PageFactory.initElements(driver, this);
    }

    public void logout() {
        logoutButton.click();
    }

    public void navigateToNoteTab() {
        this.noteTab.click();
    }

    public void navigateToCredentialsTab() {
        this.credentialsTab.click();
    }

    public void openNoteModal() {
        this.addNewNoteButton.click();
    }

    public void openCredentialModal() {
        this.addNewCredentialButton.click();
    }

    public void createOrUpdateNote(String title, String description) {
        this.noteTitleInput.sendKeys(title);
        this.noteDescriptionInput.sendKeys(description);
        this.noteDescriptionInput.submit();
    }

    public void clearInputNoteModal() {
        this.noteTitleInput.clear();
        this.noteDescriptionInput.clear();
    }

    public void createOrUpdateCredential(String url, String username, String password) {
        this.credentialUrlInput.sendKeys(url);
        this.credentialUsernameInput.sendKeys(username);
        this.credentialPasswordInput.sendKeys(password);
        this.credentialPasswordInput.submit();
    }

    public void clearInputCredentialModal() {
        this.credentialUrlInput.clear();
        this.credentialUsernameInput.clear();
        this.credentialPasswordInput.clear();
    }

    public void edit() {
        this.editButton.click();
    }

    public void delete() {
        this.deleteButton.click();
    }

    public String getNoteTitleModal() {
        return this.noteTitleInput.getAttribute("value");
    }

    public String getNoteDescriptionModal() {
        return this.noteDescriptionInput.getAttribute("value");
    }

    public String getCredentialUrlModal() {
        return this.credentialUrlInput.getAttribute("value");
    }

    public String getCredentialUsernameModal() {
        return this.credentialUsernameInput.getAttribute("value");
    }

    public String getCredentialPasswordModal() {
        return this.credentialPasswordInput.getAttribute("value");
    }
}
