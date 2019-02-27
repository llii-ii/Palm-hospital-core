package com.kasite.client.business.config;

import com.baidu.ueditor.PathFormat;
import com.baidu.ueditor.define.BaseState;
import com.baidu.ueditor.define.State;
import com.baidu.ueditor.upload.IStorageManager;
import com.kasite.core.common.config.KasiteConfig;
import org.apache.commons.io.FileUtils;

import java.io.*;

public class StorageManager implements IStorageManager {
    public static final int BUFFER_SIZE = 8192;

    public StorageManager() {
    }

    public State saveBinaryFile(byte[] data, String rootPath, String savePath) {
        File file = new File(rootPath + savePath);
        State state = valid(file);
        if (!state.isSuccess()) {
            return state;
        } else {
            try {
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
                bos.write(data);
                bos.flush();
                bos.close();
            } catch (IOException var7) {
                return new BaseState(false, 4);
            }

            state = new BaseState(true, file.getAbsolutePath());
            state.putInfo("url", PathFormat.format(savePath));
            state.putInfo("size", (long) data.length);
            state.putInfo("title", file.getName());
            return state;
        }
    }

    public State saveFileByInputStream(InputStream is, String rootPath, String savePath, long maxSize) {
        State state = null;
        File tmpFile = getTmpFile(rootPath);
        byte[] dataBuf = new byte[2048];
        BufferedInputStream bis = new BufferedInputStream(is, 8192);

        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(tmpFile), 8192);
            boolean var11 = false;

            int count;
            while ((count = bis.read(dataBuf)) != -1) {
                bos.write(dataBuf, 0, count);
            }

            bos.flush();
            bos.close();
            if (tmpFile.length() > maxSize) {
                tmpFile.delete();
                return new BaseState(false, 1);
            } else {
                state = saveTmpFile(tmpFile, rootPath + savePath);
                state.putInfo("url", PathFormat.format(savePath));
                if (!state.isSuccess()) {
                    tmpFile.delete();
                }

                return state;
            }
        } catch (IOException var12) {
            return new BaseState(false, 4);
        }
    }

    public State saveFileByInputStream(InputStream is, String rootPath, String savePath) {
        State state = null;
        File tmpFile = getTmpFile(rootPath);
        byte[] dataBuf = new byte[2048];
        BufferedInputStream bis = new BufferedInputStream(is, 8192);

        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(tmpFile), 8192);
            boolean var9 = false;

            int count;
            while ((count = bis.read(dataBuf)) != -1) {
                bos.write(dataBuf, 0, count);
            }

            bos.flush();
            bos.close();
            state = saveTmpFile(tmpFile, rootPath + savePath);
            state.putInfo("url", PathFormat.format(savePath));
            if (!state.isSuccess()) {
                tmpFile.delete();
            }

            return state;
        } catch (IOException var10) {
            return new BaseState(false, 4);
        }
    }

    private static File getTmpFile(String rootPath) {
        File tmpDir = new File(rootPath + KasiteConfig.getTempfilePath());
        if (!tmpDir.exists()) {
            tmpDir.mkdirs();
        }
        String tmpFileName = (Math.random() * 10000.0D + "").replace(".", "");
        return new File(tmpDir, tmpFileName);
    }

    private static State saveTmpFile(File tmpFile, String path) {
        State state = null;
        File targetFile = new File(path);
        if (targetFile.canWrite()) {
            return new BaseState(false, 2);
        } else {
            try {
                FileUtils.moveFile(tmpFile, targetFile);
            } catch (IOException var5) {
                return new BaseState(false, 4);
            }

            state = new BaseState(true);
            state.putInfo("size", targetFile.length());
            state.putInfo("title", targetFile.getName());
            return state;
        }
    }

    private static State valid(File file) {
        File parentPath = file.getParentFile();
        if (!parentPath.exists() && !parentPath.mkdirs()) {
            return new BaseState(false, 3);
        } else {
            return !parentPath.canWrite() ? new BaseState(false, 2) : new BaseState(true);
        }
    }
}
