package com.keendo.biz.service;

import com.keendo.architecture.exception.BizException;
import com.keendo.architecture.utils.Log;
import com.keendo.biz.service.utils.FileUtil;
import com.keendo.biz.service.utils.JsonHelper;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Created by bint on 2018/6/14.
 */
@Service
public class UploadService {

    @Autowired
    private COSService cosService;

    /**
     * 上传非敏感的图片
     *
     * @param file
     * @param directory
     * @return
     */
    public String uploadPic(MultipartFile file, String directory, String timeStampVal) throws BizException {

        if (file == null) {
            throw new BizException("上传图片为空");
        }

        Long size = file.getSize();
        if (Constants.MAX_SIZE.compareTo(size) < 0) {
            throw new BizException("上传图片大小不可超过1M");
        }

        String originalFilename = file.getOriginalFilename();
        String fileSuffix = FileUtil.getFileSuffix(originalFilename);
        String filename = timeStampVal + fileSuffix;

        String path = this.connect(directory, filename);

        String cosPicUrl = null;
        try {
            String pathJson = cosService.uploadFile(path, IOUtils.toByteArray(file.getInputStream()));
            String dataJson = JsonHelper.getStringFromJson(pathJson, "data");
            cosPicUrl = JsonHelper.getStringFromJson(dataJson, "access_url");
        } catch (IOException e) {
            Log.e(e);
        }

        return cosPicUrl;
    }


    /**
     * 上传敏感的图片
     *
     * @param file
     * @param directory
     * @return
     */
    public String uploadSensitiveFile(MultipartFile file, String directory, String fileName) {

        String path = this.connect(directory, fileName);

        String cosPicUrl = null;
        try {
            String pathJson = cosService.uploadSensitiveFile(path, IOUtils.toByteArray(file.getInputStream()));
            String dataJson = JsonHelper.getStringFromJson(pathJson, "data");
            cosPicUrl = JsonHelper.getStringFromJson(dataJson, "access_url");
        } catch (IOException e) {
            Log.e(e);
        }

        return cosPicUrl;
    }


    private String connect(String directory, String fileName) {

        if (directory.endsWith("/")) {
            String path = directory + fileName;
            return path;
        }

        return directory + "/" + fileName;
    }

    private static class Constants {

        private final static Long MAX_SIZE = 1048576L;//上传文件最大为1M

    }


}
