package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/home")
public class HomeController {

    private FileService fileService;
    private NoteService noteService;

    public HomeController(FileService fileService, NoteService noteService) {
        this.fileService = fileService;
        this.noteService = noteService;
    }

    @GetMapping
    public String getHomePage(Model model) {
        addData(model);
        return "home";
    }

    @GetMapping(value="/file/view/{fileName}")
    public ResponseEntity downloadFile(HttpServletResponse response,
                                             @PathVariable("fileName") String fileName, Model model) throws IOException {
        File file = fileService.getFile(fileName);
        // addFileNames(model);
        ByteArrayResource resource = new ByteArrayResource(file.getFileData());

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(resource);
    }

    @GetMapping("/file/delete/{fileName}")
    public String deleteFile(@PathVariable("fileName") String fileName, Model model) {
        fileService.deleteFile(fileName);
        addData(model);
        return "home";
    }

    @PostMapping
    public String addNote(Note note, Model model) {
        noteService.addNote(note);
        addData(model);
        return "home";
    }

    @PostMapping("/note/edit/{note}")
    public String editNote(@PathVariable("note")Note note, Model model) {
        noteService.addNote(note);
        addData(model);
        return "home";
    }

    private void addData(Model model) {
        model.addAttribute("fileNames", fileService.getFileNames());
        model.addAttribute("notes", noteService.getAllNotes());
    }

}
