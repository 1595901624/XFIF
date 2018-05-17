package com.lhy.xfif;

import android.os.Environment;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * SD卡文件操作
 *
 * @author lhy 2018年5月17日10:55:21
 */
public class FileUtil {

    /*任何人都不能进行构造函数操作*/
    private FileUtil() throws Exception {
        throw new Exception("不需要进行初始化操作");
    }

    /**
     * 检查SD卡是否存在 [当没有外挂SD卡时，内置ROM也被识别为存在sd卡]
     *
     * @return
     */
    public static boolean isSDCardExist() {
        final String status = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(status)) {
            return true;
        }
        return false;
    }

    /**
     * 获取SD卡根目录路径
     *
     * @return 路径
     */
    public static String getSdCardPath() {
        boolean exist = isSDCardExist();
        String sdpath = "";
        if (exist) {
            sdpath = Environment.getExternalStorageDirectory()
                    .getAbsolutePath();
        } else {
            sdpath = "SD卡不存在";
        }
        return sdpath;
    }

    /**
     * 获取默认的配置文件路径
     *
     * @return
     */
//    public static String getDefaultFilePath() {
//        String filepath = "";
//        File file = new File(Environment.getExternalStorageDirectory(),
//                DEFAULT_FILENAME);
//        if (file.exists()) {
//            filepath = file.getAbsolutePath();
//        } else {
//            filepath = "不适用";
//        }
//        return filepath;
//    }

    /***
     * 判断SD下某文件是否存在
     * @param fileName 文件路径
     * @return  布尔值 [是否存在]
     */
    public static boolean isExists(String fileName) {
        boolean flag = false;
        File file = new File(Environment.getExternalStorageDirectory(),
                fileName);
        if (file.exists())
            flag = true;
        return flag;
    }

    /**
     * 读取指定文件
     *
     * @param fileName SD下的文件路径+文件名，如:a/b.txt
     */
    public static String readFile(String fileName) {
        StringBuffer stringBuffer = new StringBuffer();
        try {
            String line = "";
            File file = new File(Environment.getExternalStorageDirectory(),
                    fileName);
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }
            bufferedReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return stringBuffer.toString();
    }


    /**
     * 向指定文件写入字符串
     *
     * @param fileName
     * @param content
     */
    public static void writeFile(String fileName, String content) {
        File file = new File(Environment.getExternalStorageDirectory(),
                fileName);
        try {
            if (!file.exists()) {
                File fileDirectory = file.getParentFile();
                if (!fileDirectory.exists()) {
                    fileDirectory.mkdirs();
                }
                file.createNewFile();
            }
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
            bufferedWriter.write(content);
            bufferedWriter.flush();
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}