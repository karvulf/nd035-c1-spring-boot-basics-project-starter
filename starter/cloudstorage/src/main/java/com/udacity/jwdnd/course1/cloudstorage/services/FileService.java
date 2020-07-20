package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
public class FileService {

    private FileMapper fileMapper;

    public FileService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    public ArrayList<String> getFileNames() {
        return fileMapper.getFileNames();
    }

    public boolean hasFileName(String fileName) {
        File file = fileMapper.getFile(fileName);
        return file != null;
    }

    public int uploadFile(MultipartFile multipartFile) {
        File file = new File(
                null,
                multipartFile.getOriginalFilename(),
                multipartFile.getContentType(),
                null,
                1,
                null);

        return fileMapper.insert(file);
    }
}
