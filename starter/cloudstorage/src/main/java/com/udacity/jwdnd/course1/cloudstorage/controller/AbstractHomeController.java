package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.EncryptionService;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public abstract class AbstractHomeController {

    private FileService fileService;
    private NoteService noteService;
    private CredentialService credentialService;
    private EncryptionService encryptionService;

    public AbstractHomeController(FileService fileService, NoteService noteService, CredentialService credentialService, EncryptionService encryptionService) {
        this.fileService = fileService;
        this.noteService = noteService;
        this.credentialService = credentialService;
        this.encryptionService = encryptionService;
    }

    public void addData(Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("fileNames", fileService.getFileNames(user.getUserId()));
        model.addAttribute("credentials", credentialService.getAllCredentials(user.getUserId()));
        model.addAttribute("notes", noteService.getAllNotes(user.getUserId()));
        model.addAttribute("encryptionService", encryptionService);
    }

    public void addAlertMsgCreate(Model model, int result, String title) {
        String displayedMsg;
        if(result < 0) {
            displayedMsg = "Oops! Something went wrong when creating your " + title + "!";
        } else {
            displayedMsg = "You successfully created your " + title + "!";
        }
        model.addAttribute("alertMsg", displayedMsg);
    }

    public void addAlertMsgDelete(Model model, int result, String title) {
        String displayedMsg;
        if(result < 0) {
            displayedMsg = "Oops! Something went wrong when deleting your " + title + "!";
        } else {
            displayedMsg = "You successfully deleted your " + title + "!";
        }
        model.addAttribute("alertMsg", displayedMsg);
    }

    public void addAlertMsgUpdate(Model model, int result, String title) {
        String displayedMsg;
        if(result < 0) {
            displayedMsg = "Oops! Something went wrong when updating your " + title + "!";
        } else {
            displayedMsg = "You successfully updated your " + title + "!";
        }
        model.addAttribute("alertMsg", displayedMsg);
    }

}
