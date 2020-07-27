package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class NoteService {

    private NoteMapper noteMapper;

    public NoteService(NoteMapper noteMapper) {
        this.noteMapper = noteMapper;
    }

    public ArrayList<Note> getAllNotes() {
        return noteMapper.getAllNotes();
    }

    public int addOrUpdateNote(Note note) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        note.setUserId(user.getUserId());
        if(note.getNoteId() != null) {
            return noteMapper.update(note);
        }
        return noteMapper.insert(note);
    }

    public void deleteNote(Integer noteId) {
        noteMapper.deleteNote(noteId);
    }
}
