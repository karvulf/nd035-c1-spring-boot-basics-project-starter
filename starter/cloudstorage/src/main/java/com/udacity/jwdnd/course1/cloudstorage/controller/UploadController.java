package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/upload")
public class UploadController {

    private FileService fileService;
    private static String FILENAMES_KEY = "fileNames";

    public UploadController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping
    public String handleUploadFile(@RequestParam("fileUpload") MultipartFile file,
                                   RedirectAttributes redirectAttributes,  Model model) {
        // check if file is empty
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Please select a file to upload.");
            return "redirect:/home";
        }

        boolean hasFileNameStored = fileService.hasFileName(file.getOriginalFilename());

        String displayedMsg;

        if(!hasFileNameStored) {
            int result = -1;
            try{
                result = fileService.uploadFile(file);
            } catch(Exception e){
                System.out.println("Error uploading file: " + e.getMessage());
            }
            if(result > 0) {
                displayedMsg = "You successfully uploaded " + file.getOriginalFilename() + "!";
                User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                model.addAttribute(FILENAMES_KEY, fileService.getFileNames(user.getUserId()));
            } else {
                displayedMsg = "Oops! Something went wrong when uploading your file " + file.getOriginalFilename() + "!";
            }
        } else {
            displayedMsg = "The file with name " + file.getOriginalFilename() + " is already stored!";
        }

        redirectAttributes.addFlashAttribute("alertMsg", displayedMsg);

        return "redirect:/home";
    }
}
