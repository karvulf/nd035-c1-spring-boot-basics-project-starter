package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;

@Service
public class CredentialService {

    private CredentialMapper credentialMapper;
    private EncryptionService encryptionService;

    public CredentialService(CredentialMapper credentialMapper, EncryptionService encryptionService) {
        this.credentialMapper = credentialMapper;
        this.encryptionService = encryptionService;
    }

    public ArrayList<Credential> getAllCredentials(Integer userId) {
        return credentialMapper.getAllCredentials(userId);
    }

    public int addOrUpdateCredential(Credential credential) {
        if(credential.getCredentialId() != null) {
            return updateCredential(credential);
        }else{
            return addCredential(credential);
        }
    }

    public int deleteCredential(Integer credentialId) {
        return credentialMapper.deleteCredential(credentialId);
    }

    private int addCredential(Credential credential) {
        String encodedKey = getRandomKey();
        String encryptedPassword = encryptionService.encryptValue(credential.getPassword(), encodedKey);
        credential.setPassword(encryptedPassword);
        credential.setKey(encodedKey);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        credential.setUserId(user.getUserId());
        return credentialMapper.insert(credential);
    }

    private int updateCredential(Credential credential) {
        String encodedKey = getRandomKey();
        String encryptedPassword = encryptionService.encryptValue(credential.getPassword(), encodedKey);
        credential.setPassword(encryptedPassword);
        credential.setKey(encodedKey);
        return credentialMapper.update(credential);
    }

    private String getRandomKey() {
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        return Base64.getEncoder().encodeToString(key);
    }
}
