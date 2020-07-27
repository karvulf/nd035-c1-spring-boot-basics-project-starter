package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.EncryptionService;
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
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/file")
public class FileController extends AbstractHomeController {

    private FileService fileService;

    public FileController(FileService fileService, NoteService noteService, CredentialService credentialService, EncryptionService encryptionService) {
        super(fileService, noteService, credentialService, encryptionService);
        this.fileService = fileService;
    }

    @GetMapping(value="/view/{fileName}")
    public ResponseEntity downloadFile(@PathVariable("fileName") String fileName, Model model) {
        File file = fileService.getFile(fileName);
        ByteArrayResource resource = new ByteArrayResource(file.getFileData());

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(resource);
    }

    @GetMapping("/delete/{fileName}")
    public String deleteFile(@PathVariable("fileName") String fileName, Model model) {
        fileService.deleteFile(fileName);
        addData(model);
        return "home";
    }
}
