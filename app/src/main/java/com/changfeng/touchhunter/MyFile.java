package com.changfeng.touchhunter;

import android.util.Log;

import org.apache.http.util.EncodingUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Created by changfeng on 2015/4/2.
 */
public class MyFile {

    private static final String TAG = "MyFile";

    public static final String sdcardDir = "/storage/sdcard0/";

    public static String readFileSdcardFile(String fileName) throws IOException {
        String res="";
        try{
            FileInputStream fin = new FileInputStream(fileName);

            int length = fin.available();

            byte [] buffer = new byte[length];
            fin.read(buffer);

            res = EncodingUtils.getString(buffer, "UTF-8");

            fin.close();
        }

        catch(Exception e){
            e.printStackTrace();
        }
        return res;
    }

    public static boolean containString(String filename, String pattern) throws IOException{
        try{
            File file = new File(filename);
            if (!file.exists()) {
                return false;
            }
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String res = null;
            while ((res = br.readLine() )!= null) {
                try {
                    if (res.contains(pattern)) {
                        fr.close();
                        br.close();
                        Log.d(TAG, "target string hunted. res:" + res);
                        return true;
                    }
                } catch (NullPointerException e) {
                    fr.close();
                    br.close();
                    Log.e(TAG, "containsString", e);
                    return true;
                }
            }
            fr.close();
            br.close();
            return false;

        } catch (Exception e) {
            Log.e(TAG, "containString()", e);
        }

        return  false;
    }

    //写文件
    public static void writeSDFile(String fileName, String write_str) throws IOException {

        File file = new File(fileName);

        FileOutputStream fos = new FileOutputStream(file);

        byte [] bytes = write_str.getBytes();

        fos.write(bytes);

        fos.close();
    }

    //写数据到SD中的文件
    public static void writeFileSdcardFile(String fileName,String write_str) throws IOException {
        try{

            FileOutputStream fout = new FileOutputStream(fileName);
            byte [] bytes = write_str.getBytes();

            fout.write(bytes);
            fout.close();
        }

        catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void appendToFile(String filename, String context) throws IOException {
        try {
            File file = new File(filename);

            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fileWriter = new FileWriter(file.getName(), true);
            fileWriter.write(context);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void writeRandomAccessSdcardFile(String filename, String write_str) throws IOException {
        try {
            RandomAccessFile file = new RandomAccessFile(filename, "rw");
            Long len = file.length();
            file.write(write_str.getBytes(), len.intValue(), write_str.length());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
