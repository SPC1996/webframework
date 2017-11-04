package com.keessi.annotation.util.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LocalFileHelper implements FileHelper {
    private Logger logger = LoggerFactory.getLogger(LocalFileHelper.class);

    private String uploadPath;

    private static final String DEFAULT_UPLOAD_PATH = ContextLoader.getCurrentWebApplicationContext().getServletContext().getRealPath("/") + "/static/upload/";

    public LocalFileHelper() {
        this.uploadPath = DEFAULT_UPLOAD_PATH;
    }

    @Override
    public String uploadOne(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return null;
        }
        Date now = new Date(System.currentTimeMillis());
        String dateString = new SimpleDateFormat("yyyy_MM_dd").format(now);
        String timeString = new SimpleDateFormat("HHmmssSSS").format(now);
        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.') + 1);
        String filePath = uploadPath + dateString;
        String fileName = timeString + "." + suffix;
        try {
            new File(filePath).mkdirs();
            File newFile = new File(filePath + File.separator + fileName);
            newFile.createNewFile();
            file.transferTo(newFile);
        } catch (IOException e) {
            logger.error("File " + file.getOriginalFilename() + " upload to local failed!");
            throw e;
        }
        return "/static/upload/" + dateString + File.separator + timeString + "." + suffix;
    }

    @Override
    public List<String> uploadSome(MultipartFile[] files) throws IOException {
        if (files.length <= 0) {
            return null;
        }
        List<String> result = new ArrayList<>();
        Date now;
        String dateString, timeString, suffix, filePath, fileName;
        for (MultipartFile file : files) {
            now = new Date(System.currentTimeMillis());
            dateString = new SimpleDateFormat("yyyy_MM_dd").format(now);
            timeString = new SimpleDateFormat("HHmmssSSS").format(now);
            suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.') + 1);
            filePath = uploadPath + dateString;
            fileName = timeString + "." + suffix;
            try {
                new File(filePath).mkdirs();
                File newFile = new File(filePath + File.separator + fileName);
                newFile.createNewFile();
                file.transferTo(newFile);
                result.add("/static/upload/" + dateString + File.separator + timeString + "." + suffix);
            } catch (IOException e) {
                logger.error("File " + file.getOriginalFilename() + " upload to local failed!");
                throw e;
            }
        }
        return result;
    }

    public String getUploadPath() {
        return uploadPath;
    }

    public void setUploadPath(String uploadPath) {
        this.uploadPath = uploadPath;
    }


}
