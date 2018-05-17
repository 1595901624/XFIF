package com.lhy.xfif;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import static com.lhy.xfif.FileUtil.isExists;
import static com.lhy.xfif.FileUtil.readFile;
import static com.lhy.xfif.FileUtil.writeFile;

/**
 * 设置config
 *
 * @author lhy 2018年5月17日12:49:13
 */
public class ConfigPreferences {
    /*文件路径*/
    private final static String DEFAULT_FILENAME = "xfif/xfif_config.lhy";
    private final static String TAG = "xfif_config_preferences";

    private ConfigPreferences() {
        /*第一次进行初始化*/
        if (!isExists(DEFAULT_FILENAME)) {
            Config firstConfig = new Config(false, 75);
            String jsonString = JSON.toJSONString(firstConfig);
            writeFile(DEFAULT_FILENAME, jsonString);
        } else {
            Log.e(TAG, "不是第一次--------");
        }
    }

    private static class ConfigPreferencesHolder {
        private final static ConfigPreferences configPreferences = new ConfigPreferences();
    }

    public static ConfigPreferences getInstance() {
        return ConfigPreferencesHolder.configPreferences;
    }

    /**
     * 获取默认路径下的配置文件
     *
     * @return config当前配置
     */
    public Config getDefaultConfig() {
        return getConfig(DEFAULT_FILENAME);
    }

    /**
     * 写入默认路径下的配置文件
     *
     * @param key   写入的key
     * @param value 写入key的值
     */
    public void putDefaultConfig(String key, Object value) {
        putConfig(DEFAULT_FILENAME, key, value);
    }

    /**
     * 写入配置文件
     *
     * @param fileName 文件路径
     * @param key      写入的key
     * @param value    写入key的值
     */
    public void putConfig(String fileName, String key, Object value) {
        String jsonString = readFile(fileName);
        JSONObject jsonObject = JSON.parseObject(jsonString);
        jsonObject.remove(key);
        jsonObject.put(key, value);
        String result = jsonObject.toString();
        writeFile(fileName, result);
    }


    /**
     * 获取配置文件
     *
     * @param fileName 文件路径
     * @return config当前配置
     */
    public Config getConfig(String fileName) {
        String jsonString = readFile(fileName);
        Config config = JSON.parseObject(jsonString, Config.class);
        return config;
    }
}