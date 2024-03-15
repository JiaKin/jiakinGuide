package com.example.jiaqiguide.Class;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.core.app.ActivityCompat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;

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

    public static void SaveJson(Context context, String Json, FileType t, String name) {
        String path = getLegalFilePath(t, name);
        try (FileOutputStream fos = new FileOutputStream(new File(path))) {
            fos.write(Json.getBytes());
            Toast.makeText(context,"Save Completed.", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(context, "Save Failed." + e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    public static <T> T readJson(Context context, FileType f, String name, Type typeOfT) {
        String path = getLegalFilePath(f, name);
        try (InputStreamReader fis = new InputStreamReader(new FileInputStream(new File(path)))) {
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
    public enum FileType{
        Audio,
        Image,
        File
    }
}
