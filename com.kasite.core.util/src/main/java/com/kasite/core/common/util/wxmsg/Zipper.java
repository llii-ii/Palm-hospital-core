package com.kasite.core.common.util.wxmsg;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

/**
 * 文件压缩辅助类
 *
 * @created Air 2015/5/26.
 */
public class Zipper {


    /**
     * 使用给定密码解压指定的ZIP压缩文件到指定目录
     * <p>
     * 如果指定目录不存在,可以自动创建,不合法的路径将导致异常被抛出
     *
     * @param zip    指定的ZIP压缩文件
     * @param dest   解压目录
     * @param passwd ZIP文件的密码
     * @return 解压后文件数组
     * @throws ZipException 压缩文件有损坏或者解压缩失败抛出
     */
    public static File[] unzip(String zip, String dest, String passwd) throws ZipException {
        File zipFile = new File(zip);
        return unzip(zipFile, dest, passwd);
    }

    /**
     * 使用给定密码解压指定的ZIP压缩文件到当前目录
     *
     * @param zip    指定的ZIP压缩文件
     * @param passwd ZIP文件的密码
     * @return 解压后文件数组
     * @throws ZipException 压缩文件有损坏或者解压缩失败抛出
     */
    public static File[] unzip(String zip, String passwd) throws ZipException {
        File zipFile = new File(zip);
        File parentDir = zipFile.getParentFile();
        return unzip(zipFile, parentDir.getAbsolutePath(), passwd);
    }
    /**
     * 使用给定密码解压指定的ZIP压缩文件到当前目录
     *
     * @param zip    指定的ZIP压缩文件
     * @param passwd ZIP文件的密码
     * @return 解压后文件数组
     * @throws ZipException 压缩文件有损坏或者解压缩失败抛出
     */
    public static File[] unzip(File zipFile, String passwd) throws ZipException {
        File parentDir = zipFile.getParentFile();
        return unzip(zipFile, parentDir.getAbsolutePath(), passwd);
    }

    /**
     * 使用给定密码解压指定的ZIP压缩文件到指定目录
     * <p>
     * 如果指定目录不存在,可以自动创建,不合法的路径将导致异常被抛出
     *
     * @param dest   解压目录
     * @param passwd ZIP文件的密码
     * @return 解压后文件数组
     * @throws ZipException 压缩文件有损坏或者解压缩失败抛出
     */
    @SuppressWarnings("unchecked")
	public static File[] unzip(File zipFile, String dest, String passwd) throws ZipException {
        ZipFile zFile = new ZipFile(zipFile);
        zFile.setFileNameCharset("UTF-8");
        if (!zFile.isValidZipFile()) {
            throw new ZipException("压缩文件不合法,可能被损坏.");
        }
        File destDir = new File(dest);
        if (destDir.isDirectory() && !destDir.exists()) {
            destDir.mkdir();
        }
        if (zFile.isEncrypted()) {
            zFile.setPassword(passwd.toCharArray());
        }
        zFile.extractAll(dest);

        List<FileHeader> headerList = zFile.getFileHeaders();
        List<File> extractedFileList = new ArrayList<File>();
        for (FileHeader fileHeader : headerList) {
            if (!fileHeader.isDirectory()) {
                extractedFileList.add(new File(destDir, fileHeader.getFileName()));
            }
        }
        File[] extractedFiles = new File[extractedFileList.size()];
        extractedFileList.toArray(extractedFiles);
        return extractedFiles;
    }

    /**
     * @param zipFile        需要解压的文件名
     * @param unzipDirectory 解压文件路径
     * @param pwd            解压密码
     * @return 成功-File 失败-null
     * @throws ZipException
     */
    public static File unzipFile(File zipFile, String unzipDirectory, String pwd) throws ZipException {
        ZipFile zFile = new ZipFile(zipFile);
        zFile.setFileNameCharset("UTF-8");
        if (!zFile.isValidZipFile()) {
            throw new ZipException("无效的压缩文件.");
        }

        if (zFile.isEncrypted()) {
            zFile.setPassword(pwd.toCharArray());
        }
        zFile.extractAll(unzipDirectory);

        return new File(unzipDirectory);
    }

    /**
     * @param unzipFile   需要压缩的文件(文件夹)
     * @param zipFileName 压缩后的文件名
     * @return 成功-File 失败-null
     * @throws ZipException
     */
    public File zipFile(File unzipFile, String zipFileName) throws ZipException {
        return zipFile(unzipFile, zipFileName, null);
    }

    /**
     * @param unzipFile   需要压缩的文件(文件夹)
     * @param zipFileName 压缩后的文件名
     * @param pwd         压缩密码
     * @return 成功-File 失败-null
     * @throws ZipException
     */
    public static File zipFile(File unzipFile, String zipFileName, String pwd) throws ZipException {
        ZipParameters parameters = new ZipParameters();
        parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
        parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
        if (!StringUtil.isEmpty(pwd)) {
            parameters.setEncryptFiles(true);
            parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_STANDARD);
            parameters.setPassword(pwd.toCharArray());
        }

        try {
            ZipFile zipFile = new ZipFile(zipFileName);
            if (!unzipFile.exists()) {
                unzipFile.mkdirs();
            }
            if (unzipFile.isDirectory()) {
                zipFile.addFolder(unzipFile, parameters);
            } else {
                zipFile.addFile(unzipFile, parameters);
            }
            return new File(zipFileName);
        } catch (ZipException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public static File zipFileForAll(File unzipFile, String zipFileName, String pwd) throws ZipException {
        //文件不存在时不压缩
        if (!unzipFile.exists()) {
            return null;
        }

        ZipParameters parameters = new ZipParameters();
        parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
        parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
        if (!StringUtil.isEmpty(pwd)) {
            parameters.setEncryptFiles(true);
            parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_STANDARD);
            parameters.setPassword(pwd.toCharArray());
        }

        File file = new File(zipFileName);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
        }

        ZipFile zipFile = new ZipFile(zipFileName);
        if (unzipFile.isDirectory()) {
//            zipFile.addFolder(unzipFile, parameters);
            File[] files = unzipFile.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    zipFile.addFolder(files[i], parameters);
                } else {
                    zipFile.addFile(files[i], parameters);
                }
            }
        } else {
            zipFile.addFile(unzipFile, parameters);
        }

        return new File(zipFileName);
    }

    /**
     * @param zipFile        需要解压的文件名
     * @param unzipDirectory 解压文件路径
     * @return 成功-File 失败-null
     * @throws ZipException
     */
    public File unzipFile(File zipFile, String unzipDirectory) throws ZipException {
        return unzipFile(zipFile, unzipDirectory, null);
    }
}
