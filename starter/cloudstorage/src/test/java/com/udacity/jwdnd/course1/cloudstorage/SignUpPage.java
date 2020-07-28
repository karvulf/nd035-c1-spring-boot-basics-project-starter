package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class SignUpPage {

    @FindBy(id = "inputUsername")
    private WebElement usernameInput;

    @FindBy(id = "inputPassword")
    private WebElement passwordInput;

    @FindBy(id = "inputFirstName")
    private WebElement firstnameInput;

    @FindBy(id = "inputLastName")
    private WebElement lastnameInput;

    @FindBy(id = "login-link")
    private WebElement loginLink;

    @FindBy(id = "success-msg")
    private WebElement successMsg;

    public SignUpPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    public void signup(String username, String password, String firstname, String lastname){
        this.usernameInput.sendKeys(username);
        this.passwordInput.sendKeys(password);
        this.firstnameInput.sendKeys(firstname);
        this.lastnameInput.sendKeys(lastname);

        this.lastnameInput.submit();
    }

    public void navigateToLogin() {
        loginLink.click();
    }

    public String getSuccessMsg() {
        return successMsg.getText();
    }
}
