package com.libs.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import android.os.Environment;
import android.util.Log;

public class LogUtil {

    public static boolean VDBG = true;
    public static boolean DDBG = true;
    public static boolean IDBG = true;
    public static boolean WDBG = true;
    public static boolean EDBG = true;
    public static boolean LOG2FILE = false;
    
    public static void setDebugEnabled(boolean b) {
        VDBG = b;
        DDBG = b;
        IDBG = b;
        WDBG = b;
        EDBG = b;
    }
    
    public static void v(String tag, String msg) {
        if (VDBG) {
            Log.v(tag, msg);
            if (LOG2FILE)
                out(tag, msg);
        }
    }
    
    public static void d(String tag, String msg) {
        if (DDBG) {
            Log.d(tag, msg);
            if (LOG2FILE)
                out(tag, msg);
        }
    }
    
    public static void i(String tag, String msg) {
        if(IDBG) {
            Log.i(tag, msg);
            if (LOG2FILE)
                out(tag, msg);
        }
    }
    
    public static void w(String tag, String msg) {
        if(WDBG) {
            Log.w(tag, msg);
            if (LOG2FILE)
                out(tag, msg);
        }
    }
    
    public static void e(String tag, String msg) {
        if(EDBG) {
            Log.e(tag, msg);
            if (LOG2FILE)
                out(tag, msg);
        }
    }
    
    private synchronized static void out(String tag, String msg){
        if (Utils.isSDCardMounted()) {
            FileOutputStream output = null;
            File file = new File(Environment.getExternalStorageDirectory(), "xlog.txt");
            try{
                long size = file.length();
                if(size > 1024 * 1024 * 16) {
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }catch(SecurityException e) {
                e.printStackTrace();
            }
            try {
                output = new FileOutputStream(file, true);
                Date date = new Date();
                String xtime = date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds();
                String msgdata = xtime + " " + tag + " " + msg + "\r\n";
                output.write(msgdata.getBytes("UTF-8"));
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    output.close();
                } catch (IOException ee) {
                    ee.printStackTrace();
                }
            }
        }
    }
    
}
