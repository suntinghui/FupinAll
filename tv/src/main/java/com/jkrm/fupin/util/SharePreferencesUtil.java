package com.jkrm.fupin.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.jkrm.fupin.base.MyApp;

import java.util.Set;

/**
 * Created by hzw on 2018/7/18.
 */
public class SharePreferencesUtil {

    /**
     * 保存 int
     * @param xmlName xml文件名
     * @param key     要保存的 key
     * @param value   要保存的 value
     */
    public static final void saveInt(String xmlName, String key, int value) {
        SharedPreferences sharedPreferences = MyApp.getInstance().getSharedPreferences(xmlName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();

    }

    /**
     * 保存string数组
     * @param xmlName   xml文件名
     * @param key       要保存的 key
     * @param stringSet 要保存的 value
     */
    public static final void savaStringSet(String xmlName, String key, Set<String> stringSet) {
        SharedPreferences sharedPreferences = MyApp.getInstance().getSharedPreferences(xmlName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(key, stringSet);
        editor.commit();
    }

    /**
     * 获取string数组
     * @param xmlName
     * @param key
     * @param stringSet
     * @return
     */
    public static final Set<String> getStringSet(String xmlName, String key, Set<String> stringSet) {
        SharedPreferences sharedPreferences = MyApp.getInstance().getApplicationContext().getSharedPreferences(xmlName, Context.MODE_PRIVATE);
        Set<String> value = sharedPreferences.getStringSet(key, stringSet);
        return value;
    }


    /**
     * 保存 String
     * @param xmlName xml文件名
     * @param key     要保存的 key
     * @param value   要保存的 value
     */
    public static final void saveString(String xmlName, String key, String value) {
        SharedPreferences sharedPreferences = MyApp.getInstance().getSharedPreferences(xmlName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();

    }

    /**
     * 保存 boolean
     *
     * @param xmlName xml文件名
     * @param key     要保存的 key
     * @param value   要保存的 value
     */
    public static final void saveBoolean(String xmlName, String key, boolean value) {
        SharedPreferences sharedPreferences = MyApp.getInstance().getSharedPreferences(xmlName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();

    }

    /**
     * 保存 float
     *
     * @param xmlName xml文件名
     * @param key     要保存的 key
     * @param value   要保存的 value
     */
    public static final void saveFloat(String xmlName, String key, float value) {
        SharedPreferences sharedPreferences = MyApp.getInstance().getSharedPreferences(xmlName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(key, value);
        editor.commit();

    }

    /**
     * 保存 long
     *
     * @param xmlName xml文件名
     * @param key     要保存的 key
     * @param value   要保存的 value
     */
    public static final void saveLong(String xmlName, String key, long value) {
        SharedPreferences sharedPreferences = MyApp.getInstance().getSharedPreferences(xmlName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(key, value);
        editor.commit();

    }

    /**
     * 保存 Set < String >
     *
     * @param xmlName xml文件名
     * @param key     要保存的 key
     * @param value   要保存的 value
     */
    public static final void saveSet(String xmlName, String key, Set<String> value) {
        SharedPreferences sharedPreferences = MyApp.getInstance().getSharedPreferences(xmlName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(key, value);
        editor.commit();

    }

    /**
     * 清除当前xml 某一个key对应的值
     *
     * @param xmlName xml文件名
     * @param key     要清除的key
     */
    public static void clearOne(String xmlName, String key) {
        SharedPreferences sharedPreferences = MyApp.getInstance().getSharedPreferences(xmlName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.commit();
    }

    /**
     * 清除xml中所有的值
     *
     * @param xmlName xml文件名
     */
    public static void clearAll(String xmlName) {
        SharedPreferences sharedPreferences = MyApp.getInstance().getSharedPreferences(xmlName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }

    /**
     * 获取int 值
     *
     * @param xmlName      xml文件名
     * @param key          要获取的key
     * @param defaultValue 默认值
     * @return
     */
    public static int getInt(String xmlName, String key, int defaultValue) {
        SharedPreferences sharedPreferences = MyApp.getInstance().getSharedPreferences(xmlName, Context.MODE_PRIVATE);
        int value = sharedPreferences.getInt(key, defaultValue);
        return value;
    }

    /**
     * 获取 String 值
     *
     * @param xmlName      xml文件名
     * @param key          要获取的key
     * @param defaultValue 默认值
     * @return
     */
    public static String getString(String xmlName, String key, String defaultValue) {

        SharedPreferences sharedPreferences = MyApp.getInstance().getApplicationContext().getSharedPreferences(xmlName, Context.MODE_PRIVATE);
        String value = sharedPreferences.getString(key, defaultValue);
        return value;
    }

    /**
     * 获取Boolean 值
     *
     * @param xmlName      xml文件名
     * @param key          要获取的key
     * @param defaultValue 默认值
     * @return
     */
    public static boolean getBoolean(String xmlName, String key, boolean defaultValue) {
        SharedPreferences sharedPreferences = MyApp.getInstance().getSharedPreferences(xmlName, Context.MODE_PRIVATE);
        boolean value = sharedPreferences.getBoolean(key, defaultValue);
        return value;
    }

    /**
     * 获取long 值
     *
     * @param xmlName      xml文件名
     * @param key          要获取的key
     * @param defaultValue 默认值
     * @return
     */
    public static long getLong(String xmlName, String key, long defaultValue) {
        SharedPreferences sharedPreferences = MyApp.getInstance().getSharedPreferences(xmlName, Context.MODE_PRIVATE);
        long value = sharedPreferences.getLong(key, defaultValue);
        return value;
    }

    /**
     * 获取float 值
     *
     * @param xmlName      xml文件名
     * @param key          要获取的key
     * @param defaultValue 默认值
     * @return
     */
    public static float getFloat(String xmlName, String key, float defaultValue) {
        SharedPreferences sharedPreferences = MyApp.getInstance().getSharedPreferences(xmlName, Context.MODE_PRIVATE);
        float value = sharedPreferences.getFloat(key, defaultValue);
        return value;
    }
}
