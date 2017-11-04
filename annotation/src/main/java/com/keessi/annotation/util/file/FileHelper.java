package com.keessi.annotation.util.file;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FileHelper {
    String uploadOne(MultipartFile file) throws IOException;

    List<String> uploadSome(MultipartFile[] files) throws IOException;
}
