package de.grafelhaft.winespray.app.util;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

/**
 * Created by @author Markus Graf (Grafelhaft) on 16.10.2016.
 */

public class FileUtils {

    public static final String PATH_ROOT = "/WineSpray/";
    //public static final String PATH_IMPORT = PATH_ROOT + "Import/";
    public static final String PATH_IMPORT = "/Download/";
    public static final String PATH_EXPORT = PATH_ROOT + "Export/";

    private static boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    private static boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    public static boolean initDirs() {
        File dir = new File(Environment.getExternalStorageDirectory() + File.separator + PATH_IMPORT);
        return dir.mkdirs();
    }

    public static File writeFile(String path, String fileName, String content) {
        if (isExternalStorageAvailable() && !isExternalStorageReadOnly()) {
            String pathDir = Environment.getExternalStorageDirectory() + path;
            File dir = new File(pathDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            File file = new File(dir, fileName);

            try {
                FileOutputStream outputStream = new FileOutputStream(file);
                outputStream.write(content.getBytes());
                outputStream.close();
                return file;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static String readFile(String path) {
        File file;
        if (path.contains(Environment.getExternalStorageDirectory().getPath())) {
            file = new File(path);
        } else {
            file = new File(Environment.getExternalStorageDirectory() + path);
        }
        if (file.exists()) {
            StringBuilder stringBuilder = new StringBuilder();

            try {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line;

                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                    stringBuilder.append("\n");
                }

                reader.close();

                return stringBuilder.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public static File[] listFiles(String path) {
        File dir = new File(Environment.getExternalStorageDirectory() + path);
        if (dir.exists()) {
            return dir.listFiles();
        }
        return null;
    }

    public static File[] listFiles(File dir) {
        if (dir.isDirectory()) {
            if (dir.exists()) {
                return dir.listFiles();
            }
        }
        return null;
    }

    public static boolean deleteFile(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                deleteFile(f);
            }
        } else {
             return file.delete();
        }
        return false;
    }

}
