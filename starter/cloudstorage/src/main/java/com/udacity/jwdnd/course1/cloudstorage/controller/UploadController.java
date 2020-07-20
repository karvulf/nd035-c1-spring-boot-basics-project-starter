package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Controller
@RequestMapping("/upload")
public class UploadController {

    private FileService fileService;
    private static String FILENAMES_KEY = "fileNames";

    public UploadController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping
    public String getUpload(Model model) {
        model.addAttribute(FILENAMES_KEY, fileService.getFileNames());
        return "home";
    }

    @PostMapping
    public String handleUploadFile(@RequestParam("fileUpload") MultipartFile file,
                                   RedirectAttributes redirectAttributes, Model model) {

        // check if file is empty
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Please select a file to upload.");
            return "redirect:/home";
        }

        boolean hasFileNameStored = fileService.hasFileName(file.getOriginalFilename());

        String displayedMsg;

        if(!hasFileNameStored) {
            int result = fileService.uploadFile(file);
            if(result > 0) {
                displayedMsg = "You successfully uploaded " + file.getOriginalFilename() + "!";
                model.addAttribute(FILENAMES_KEY, fileService.getFileNames());
            } else {
                displayedMsg = "Oops! Something went wrong when uploading your file " + file.getOriginalFilename() + "!";
            }
        } else {
            displayedMsg = "The file " + file.getOriginalFilename() + " is already stored!";
        }

        redirectAttributes.addFlashAttribute("alertMsg", displayedMsg);

        return "redirect:/home";
    }
}
