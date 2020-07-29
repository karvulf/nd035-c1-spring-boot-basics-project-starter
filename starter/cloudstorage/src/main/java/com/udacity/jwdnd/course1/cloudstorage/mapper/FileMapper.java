package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.apache.ibatis.annotations.*;

import java.util.ArrayList;

@Mapper
public interface FileMapper {
    @Select("SELECT * FROM FILES WHERE filename = #{fileName}")
    File getFile(String fileName);

    @Select("SELECT fileName FROM FILES WHERE userId = #{userId}")
    ArrayList<String> getFileNames(Integer userId);

    @Insert("INSERT INTO FILES (fileName, contentType, fileSize, userId, fileData) " +
            "VALUES(#{fileName}, #{contentType}, #{fileSize}, #{userId}, #{fileData})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    int insert(File file);

    @Delete("DELETE FROM FILES WHERE filename = #{fileName}")
    int deleteFile(String fileName);

}
