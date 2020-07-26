package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.apache.ibatis.annotations.*;

import java.util.ArrayList;

@Mapper
public interface NoteMapper {
    @Select("SELECT * FROM NOTES WHERE noteTitle = #{noteTitle}")
    Note getNote(String noteTitle);

    @Select("SELECT * FROM NOTES")
    ArrayList<Note> getAllNotes();

    @Insert("INSERT INTO NOTES (noteTitle, noteDescription, userId) " +
            "VALUES(#{noteTitle}, #{noteDescription}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "noteId")
    int insert(Note note);

    @Delete("DELETE FROM NOTES WHERE noteTitle = #{noteTitle}")
    void deleteNote(String noteTitle);
}
