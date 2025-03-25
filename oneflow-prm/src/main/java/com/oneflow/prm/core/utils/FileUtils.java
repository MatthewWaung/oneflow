package com.oneflow.prm.core.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileUtils {

    private final static Logger logger = LoggerFactory.getLogger(FileUtils.class);

    /*参考https://blog.csdn.net/m0_58680865/article/details/120261579*/

    /**
     * 根据文件路径获取文件
     *
     * @param filePath 文件路径
     * @return 文件
     */
    public static File getFileByPath(String filePath) {
        return StringUtils.isEmpty(filePath) ? null : new File(filePath);
    }

    /**
     * 判断文件是否存在
     *
     * @param filePath 文件路径
     * @return {@code true}: 存在<br>{@code false}: 不存在
     */
    public static boolean isFileExists(String filePath) {
        return isFileExists(getFileByPath(filePath));
    }

    /**
     * 判断文件是否存在
     *
     * @param file 文件
     * @return {@code true}: 存在<br>{@code false}: 不存在
     */
    public static boolean isFileExists(File file) {
        return file != null && file.exists();
    }

    /**
     * 判断是否是目录
     *
     * @param dirPath 目录路径
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static boolean isDir(String dirPath) {
        return isDir(getFileByPath(dirPath));
    }

    /**
     * 判断是否是目录
     *
     * @param file 文件
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static boolean isDir(File file) {
        return isFileExists(file) && file.isDirectory();
    }

    /**
     * 判断是否是文件
     *
     * @param filePath 文件路径
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static boolean isFile(String filePath) {
        return isFile(getFileByPath(filePath));
    }

    /**
     * 判断是否是文件
     *
     * @param file 文件
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static boolean isFile(File file) {
        return isFileExists(file) && file.isFile();
    }

    /**
     * 创建目录或文件
     *
     * @param file
     */
    public static void makeDir(File file) {
        if (!file.exists() && !file.isDirectory()) {
            boolean mkdirs = file.mkdirs();
            if (!mkdirs) {
                logger.info("create directory fail!");
            }
        } else {
            try {
                file.createNewFile();
            } catch (IOException e) {
                logger.info("create file fail!");
            }
        }
    }

    /**
     * 通过文件服务器的url在当前服务器上创建文件（通过url下载文件到本地）
     *
     * @param urlAddress
     * @param fileName
     * @param destinationDir
     */
    public static void fileUrl(String urlAddress, String fileName, String destinationDir) {
        OutputStream outputStream = null;
        URLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            URL url = new URL(urlAddress);
            outputStream = new BufferedOutputStream(Files.newOutputStream(Paths.get(destinationDir + "/" + fileName)));
            urlConnection = url.openConnection();
            inputStream = urlConnection.getInputStream();

            byte[] buf = new byte[8192];
            int byteRead, byteWritten = 0;
            while ((byteRead = inputStream.read(buf)) != -1) {
                outputStream.write(buf, 0, byteRead);
                outputStream.flush();
                byteWritten += byteRead;
            }
        } catch (IOException e) {
            logger.info("FileUtils fileUrl exception.", e);
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                logger.info("FileUtils fileUrl exception.", e);
            }
        }
    }


    /**
     * 创建临时文件
     *
     * @param fileName       临时文件的名称
     * @param prefix         临时文件的前缀
     * @param deleteOnExit   是否在 JVM 退出时删除临时文件
     * @return 创建的临时文件
     */
    public static File createTempFile(String fileName, String prefix, boolean deleteOnExit) {
        try {
            // 创建临时文件
            File tempFile = File.createTempFile(prefix, fileName);
            if (deleteOnExit) {
                tempFile.deleteOnExit();
            }
            return tempFile;
        } catch (IOException e) {
            logger.error("Failed to create temporary file.", e);
            return null;
        }
    }

    /**
     * 将 File 对象转换为 MultipartFile 对象
     *
     * @param tempFile 要转换的 File 对象
     * @return 转换后的 MultipartFile 对象
     */
    public static MultipartFile fileToMultipartFile(File tempFile) {
        try {
            if (tempFile == null || !tempFile.exists()) {
                logger.error("File does not exist.");
                return null;
            }
            FileInputStream fileInputStream = new FileInputStream(tempFile);
            return new MockMultipartFile(tempFile.getName(), tempFile.getName(),
                    "application/octet-stream", fileInputStream);
        } catch (IOException e) {
            logger.error("Failed to convert File to MultipartFile.", e);
            return null;
        }
    }

}
