package com.example.jiaqiguide.Class;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;

public class DefaultPath {
    public static String getPath(FileType t) {
        File f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), t.toString());
        if (!f.exists())
            f.mkdir();
        return f.getPath();
    }

    public static String getLegalFilePath(FileType t, String name) {
        String ss = getPath(t);
        if (ss == null) return "";
        return ss + "/" + name.replaceAll("[<>:\"/\\\\|?*]", "").trim();
    }

    public static void SaveJson(Context context, String Json, FileType t, String name, Activity activity) {
        String path = getLegalFilePath(t, name);
        File file = new File(path);
        ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(Json.getBytes());
            Toast.makeText(context,"Save Completed."+fos.getFD().toString(), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(context, "Save Failed." + e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    public static <T> T readJson(Context context, FileType f, String name, Type typeOfT) {
        String path = getLegalFilePath(f, name);
        File file = new File(path);
        try (InputStreamReader fis = new InputStreamReader(new FileInputStream(file))) {
            Gson gson = new Gson();

            T result = gson.fromJson(fis, typeOfT);
            return result;
        } catch (FileNotFoundException e) {
            Toast.makeText(context, "File not found.", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(context, "Loading data failed.", Toast.LENGTH_LONG).show();
        }
        return null;
    }

    public static boolean isFileExist(String name){
        String path = getLegalFilePath(FileType.File, name);
        try{
            new Gson().fromJson(path, Object.class);
        }catch (Exception e){
            return false;
        }
        return true;
    }
    public enum FileType{
        Audio,
        Image,
        File
    }
}
