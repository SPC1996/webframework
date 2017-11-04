package com.keessi.xml.util.file;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RemoteFileHelper implements FileHelper {
    private Logger logger = LoggerFactory.getLogger(RemoteFileHelper.class);

    private static final String DEFAULT_ACCESS_KEY = "FDer4BWny5sOBMURDOwDDGP-glZ3qw5HMMOwkue3";
    private static final String DEFAULT_SECRET_KEY = "vM6y2cLz-PmlGGJZknGg_1dQQyMiu9u9qgNVQm2d";
    private static final String DEFAULT_BUCKET_NAME = "cyber-lzqj";
    private static final String DEFAULT_DOMAIN = "http://on48cpql4.bkt.clouddn.com/";

    private String accessKey;
    private String secretKey;
    private String bucketName;
    private String domain;

    private Auth auth;
    private UploadManager uploadManager;

    public RemoteFileHelper() {
        this.accessKey = DEFAULT_ACCESS_KEY;
        this.secretKey = DEFAULT_SECRET_KEY;
        this.bucketName = DEFAULT_BUCKET_NAME;
        this.domain = DEFAULT_DOMAIN;
        init();
    }

    private void init() {
        auth = Auth.create(accessKey, secretKey);
        uploadManager = new UploadManager(new Configuration(Zone.autoZone()));
    }

    private String getUpToken() {
        return auth.uploadToken(bucketName);
    }

    @Override
    public String uploadOne(MultipartFile file) throws IOException {
        String result = null;
        if (file.isEmpty()) {
            return null;
        }
        try {
            Response response = uploadManager.put(file.getInputStream(), System.currentTimeMillis() + "_" + file.getOriginalFilename(), getUpToken(), null, null);
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            result = domain + putRet.key;
        } catch (QiniuException ex) {
            Response r = ex.response;
            logger.error(r.toString());
            logger.error(r.bodyString());
        }
        return result;
    }

    @Override
    public List<String> uploadSome(MultipartFile[] files) throws IOException {
        if (files.length <= 0) {
            return null;
        }
        List<String> result = new ArrayList<>();
        for (MultipartFile file : files) {
            try {
                Response response = uploadManager.put(file.getInputStream(), System.currentTimeMillis() + "_" + file.getOriginalFilename(), getUpToken(), null, null);
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                result.add(domain + putRet.key);
            } catch (QiniuException ex) {
                Response r = ex.response;
                logger.error(r.toString());
                logger.error(r.bodyString());
            }
        }
        return result;
    }

}
