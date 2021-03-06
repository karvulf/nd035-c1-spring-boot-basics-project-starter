package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.EncryptionService;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/credential")
public class CredentialController extends AbstractHomeController{

    private CredentialService credentialService;

    public CredentialController(FileService fileService, NoteService noteService, CredentialService credentialService, EncryptionService encryptionService) {
        super(fileService, noteService, credentialService, encryptionService);
        this.credentialService = credentialService;
    }


    @PostMapping
    public String addCredential(Credential credential, Model model) {
        boolean isUpdating = credential.getCredentialId() != null;
        int result = credentialService.addOrUpdateCredential(credential);
        if(isUpdating) {
            addAlertMsgUpdate(model, result, "credential");
        } else {
            addAlertMsgCreate(model, result, "credential");
        }
        addData(model);
        return "home";
    }

    @GetMapping("/delete/{credentialId}")
    public String deleteNote(@PathVariable("credentialId") Integer credentialId, Model model) {
        int result = credentialService.deleteCredential(credentialId);
        addAlertMsgDelete(model, result, "credential");
        addData(model);
        return "home";
    }
}
