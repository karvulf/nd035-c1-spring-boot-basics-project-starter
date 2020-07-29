package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
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
@RequestMapping("/note")
public class NoteController extends AbstractHomeController {

    private NoteService noteService;

    public NoteController(FileService fileService, NoteService noteService, CredentialService credentialService, EncryptionService encryptionService) {
        super(fileService, noteService, credentialService, encryptionService);
        this.noteService = noteService;
    }

    @PostMapping
    public String addNote(Note note, Model model) {
        boolean isUpdating = note.getNoteId() != null;
        int result = noteService.addOrUpdateNote(note);
        if(isUpdating) {
            addAlertMsgUpdate(model, result, "note");
        } else {
            addAlertMsgCreate(model, result, "note");
        }
        addData(model);
        return "home";
    }
    @GetMapping("/delete/{noteId}")
    public String deleteNote(@PathVariable("noteId") Integer noteId, Model model) {
        int result = noteService.deleteNote(noteId);
        addAlertMsgDelete(model, result, "note");
        addData(model);
        return "home";
    }
}
