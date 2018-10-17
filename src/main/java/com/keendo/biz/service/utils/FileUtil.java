package com.keendo.biz.service.utils;

public class FileUtil {

    /**
     * 获取文件名后缀 例如 ".jpg"
     *
     * @param filename
     * @return
     */
    public static String getFileSuffix(String filename) {
        int lastIndex = filename.lastIndexOf(".");

        String suffix = null;

        if (lastIndex != -1) {
            suffix = filename.substring(lastIndex);
        }

        return suffix;
    }
}
