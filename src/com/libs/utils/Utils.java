package com.libs.utils;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.KeyguardManager;
import android.app.ProgressDialog;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Rect;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.Toast;

public class Utils {
    private static final String TAG = Utils.class.getSimpleName();

    public static void removePref(Context context, String name) {
        SharedPreferences.Editor editPrefs = PreferenceManager
                .getDefaultSharedPreferences(context).edit();
        editPrefs.remove(name);
        editPrefs.commit();
    }

    public static String getStringPref(Context context, String name, String def) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(name, def);
    }

    public static void setStringPref(Context context, String name, String value) {
        SharedPreferences.Editor editPrefs = PreferenceManager
                .getDefaultSharedPreferences(context).edit();
        editPrefs.putString(name, value);
        editPrefs.commit();
    }

    public static int getIntPref(Context context, String name, int def) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getInt(name, def);
    }

    public static void setIntPref(Context context, String name, int value) {
        SharedPreferences.Editor editPrefs = PreferenceManager
                .getDefaultSharedPreferences(context).edit();
        editPrefs.putInt(name, value);
        editPrefs.commit();
    }

    public static boolean getBooleanPref(Context context, String name, boolean def) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean(name, def);
    }

    public static void setBooleanPref(Context context, String name, boolean value) {
        SharedPreferences.Editor editPrefs = PreferenceManager
                .getDefaultSharedPreferences(context).edit();
        editPrefs.putBoolean(name, value);
        editPrefs.commit();
    }

    public static long getLongPref(Context context, String name, long def) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getLong(name, def);
    }

    public static void setLongPref(Context context, String name, long value) {
        SharedPreferences.Editor editPrefs = PreferenceManager
                .getDefaultSharedPreferences(context).edit();
        editPrefs.putLong(name, value);
        editPrefs.commit();
    }

    // mode: Context.MODE_WORLD_READABLE
    public static String getStringPref(Context context, String name, String def, int mode) {
        SharedPreferences prefs = 
            context.getSharedPreferences(context.getPackageName() + "_preferences", mode);
        return prefs.getString(name, def);
    }
    
    public static void setStringPref(Context context, String name, String value, int mode) {
        SharedPreferences.Editor editPrefs = 
            context.getSharedPreferences(context.getPackageName() + "_preferences", mode).edit();
        editPrefs.putString(name, value);
        editPrefs.commit();
    }

    public static int getIntPref(Context context, String name, int def, int mode) {
        SharedPreferences prefs = 
            context.getSharedPreferences(context.getPackageName() + "_preferences", mode);
        return prefs.getInt(name, def);
    }

    public static void setIntPref(Context context, String name, int value, int mode) {
        SharedPreferences.Editor editPrefs = 
            context.getSharedPreferences(context.getPackageName() + "_preferences", mode).edit();
        editPrefs.putInt(name, value);
        editPrefs.commit();
    }

    public static boolean getBooleanPref(Context context, String name, boolean def, int mode) {
        SharedPreferences prefs = 
            context.getSharedPreferences(context.getPackageName() + "_preferences", mode);
        return prefs.getBoolean(name, def);
    }

    public static void setBooleanPref(Context context, String name, boolean value, int mode) {
        SharedPreferences.Editor editPrefs = 
            context.getSharedPreferences(context.getPackageName() + "_preferences", mode).edit();
        editPrefs.putBoolean(name, value);
        editPrefs.commit();
    }

    public static long getLongPref(Context context, String name, long def, int mode) {
        SharedPreferences prefs = 
            context.getSharedPreferences(context.getPackageName() + "_preferences", mode);
        return prefs.getLong(name, def);
    }

    public static void setLongPref(Context context, String name, long value, int mode) {
        SharedPreferences.Editor editPrefs = 
            context.getSharedPreferences(context.getPackageName() + "_preferences", mode).edit();
        editPrefs.putLong(name, value);
        editPrefs.commit();
    }

    
    public static void showMessage(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void showMessage(Context context, int resid) {
        Toast.makeText(context, resid, Toast.LENGTH_SHORT).show();
    }

    public static AlertDialog showDialog(Context context, CharSequence title, CharSequence msg,
            CharSequence leftBtn, DialogInterface.OnClickListener leftListener,
            CharSequence rightBtn, DialogInterface.OnClickListener rightListener,
            CharSequence middleBtn, DialogInterface.OnClickListener middleListener,
            DialogInterface.OnCancelListener cancelListener)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (title != null)
            builder.setTitle(title);
        if (msg != null)
            builder.setMessage(msg);
        if (leftBtn != null)
            builder.setPositiveButton(leftBtn, leftListener);
        if (rightBtn != null)
            builder.setNegativeButton(rightBtn, rightListener);
        if (middleBtn != null)
            builder.setNeutralButton(middleBtn, middleListener);
        if (cancelListener != null)
            builder.setOnCancelListener(cancelListener);
        builder.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                switch (keyCode) {
                    case KeyEvent.KEYCODE_SEARCH:
                        return true;
                }
                return false;
            }
        });
        
        return builder.show();
    }

    public static AlertDialog showDialog(Context context, CharSequence title, CharSequence msg) {
        return showDialog(context, title, msg, null, null, null, null, null, null, null);
    }

    public static AlertDialog showDialog(Context context, CharSequence title, CharSequence msg, DialogInterface.OnCancelListener cancelListener) {
        return showDialog(context, title, msg, null, null, null, null, null, null, cancelListener);
    }

    public static AlertDialog showDialog(Context context, CharSequence title, CharSequence msg,
            CharSequence btn, DialogInterface.OnClickListener listener)
    {
        return showDialog(context, title, msg, btn, listener, null, null, null, null, null);
    }

    public static AlertDialog showDialog(Context context, CharSequence title, CharSequence msg,
            CharSequence btn, DialogInterface.OnClickListener listener, DialogInterface.OnCancelListener cancelListener)
    {
        return showDialog(context, title, msg, btn, listener, null, null, null, null, cancelListener);
    }

    public static AlertDialog showDialog(Context context, CharSequence title, CharSequence msg,
            CharSequence leftBtn, DialogInterface.OnClickListener leftListener,
            CharSequence rightBtn, DialogInterface.OnClickListener rightListener) {
        return showDialog(context, title, msg, leftBtn, leftListener, rightBtn, rightListener, null, null, null);
    }

    public static AlertDialog showDialog(Context context, CharSequence title, CharSequence msg,
            CharSequence leftBtn, DialogInterface.OnClickListener leftListener,
            CharSequence rightBtn, DialogInterface.OnClickListener rightListener,
            DialogInterface.OnCancelListener cancelListener) {
        return showDialog(context, title, msg, leftBtn, leftListener, rightBtn, rightListener, null, null, cancelListener);
    }

    public static ProgressDialog showProgressDialog(Context context, CharSequence title, CharSequence message,
            boolean indeterminate, boolean cancelable) {
        ProgressDialog progressDialog = new ProgressDialog(context);

        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.setIndeterminate(indeterminate);
        progressDialog.setCancelable(cancelable);
        progressDialog.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                switch (keyCode) {
                    case KeyEvent.KEYCODE_SEARCH:
                        return true;
                }
                return false;
            }
        });
        progressDialog.show();

        return progressDialog;
    }

    public static void dismissDialog(Dialog dialog) {
        if (isDialogActive(dialog)) {
            dialog.dismiss();
        }
    }
    
    public static boolean isDialogActive(Dialog dialog) {
        return dialog != null && dialog.isShowing();
    }

    
    public static void handleException(Exception e) {
        e.printStackTrace();
    }
    
    public static float getDeviceDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    public static String getLocale(Context context) {
        return context.getResources().getConfiguration().locale.toString();
    }

    public static Rect getScreenRect(Context context) {
        int w = context.getResources().getDisplayMetrics().widthPixels;
        int h = context.getResources().getDisplayMetrics().heightPixels;
        return new Rect(0, 0, w, h);
    }
    
    public static boolean isSDCardMounted() {
        return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
    }

    public static File getSDCardDir() {
        return android.os.Environment.getExternalStorageDirectory();
    }

    public static String getIMEI(Context context) {
        TelephonyManager telephonyManager=(TelephonyManager) 
                context.getSystemService(Context.TELEPHONY_SERVICE);
        String imei = telephonyManager.getDeviceId();
        return imei;
    }

    public static String getMacAddress(Context context) {
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        return info.getMacAddress();
    }
    
    public static String getDeviceId(Context context) {
        String deviceId = getIMEI(context);
        if (TextUtils.isEmpty(deviceId))
            deviceId = getMacAddress(context);
        return deviceId;
    }
    
    public static String getSimId(Context context) {
        TelephonyManager telephonyManager=(TelephonyManager) 
                context.getSystemService(Context.TELEPHONY_SERVICE);
        String imsi = telephonyManager.getSubscriberId();
//        String ICCID = telephonyManager.getSimSerialNumber();
        return imsi;
    }

    public static String getVersionName(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {
        }
        return "";
    }

    public static int getVersionCode(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (NameNotFoundException e) {
        }
        return -1;
    }

    public static String getDeviceInfoForUserAgent(Context context) {
        //HTC/g7/Android/ android2.0/25/1.0.9 Build0711A/ zh-CN
        //HTC/HTC Desire/android/10/54/1.1.12 0930A/zh-CN
        StringBuilder str=new StringBuilder();
        str.append(android.os.Build.MANUFACTURER);
        str.append("/");
        str.append(android.os.Build.MODEL);
        str.append("/");
        str.append("Android");
        str.append("/");
        str.append(android.os.Build.VERSION.SDK_INT);
        str.append("/");
        str.append(getVersionCode(context));
        str.append("/");
        str.append(getVersionName(context));
        str.append("/");
        // Locale.getDefault().getLanguage()+"-"+Locale.getDefault().getCountry()
        str.append(getLocale(context));
        return str.toString();
    }
    
    
    
    public static void installPackage(Context context, String path) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(path));
        String type = "application/vnd.android.package-archive";
        intent.setDataAndType(uri, type);
        context.startActivity(intent);
    }

    public static void uninstallPackage(Activity context, String pkgName) {
        Intent intent = new Intent(Intent.ACTION_DELETE);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri data = Uri.fromParts("package", pkgName, null);
        intent.setData(data);
        context.startActivityForResult(intent, 100);
    }
    
    public static boolean isPackageExists(Context context, String packageName) {
        PackageManager pm = context.getPackageManager();
        ApplicationInfo appInfo = null;
        boolean errFlag = false;
        try {
            appInfo = pm.getApplicationInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
        } catch (NameNotFoundException e) {
            errFlag = true;
        }
        if(appInfo == null || errFlag) {
            return false;
        }
        return true;
    }
    
    public static String getMetaDataInManifest(Context context, String key) {
        String ret = "";
        try {
            ApplicationInfo ai;
            ai = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(),
                    PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            if (bundle != null)
                ret = (String) bundle.get(key);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public static int getIconResId(Context context) {
        return context.getApplicationInfo().icon;
    }

    public static int getLabelResId(Context context) {
        return context.getApplicationInfo().labelRes;
    }

    public static CharSequence getLabel(Context context) {
        return context.getPackageManager().getApplicationLabel(context.getApplicationInfo());
    }

    public static boolean getAirplaneMode(Context context) {
        boolean on = false;
        try {
            int r = Settings.System.getInt(context.getContentResolver(),
                    Settings.System.AIRPLANE_MODE_ON);
            on = r == 1 ? true : false;
        } catch (SettingNotFoundException e) {
            e.printStackTrace();
        }
        return on;
    }
    
    public static void setAirplaneMode(Context context, boolean airplaneModeOn) {
        // Change the system setting
        Settings.System.putInt(context.getContentResolver(),
                Settings.System.AIRPLANE_MODE_ON, airplaneModeOn ? 1 : 0);
        // Post the intent
        Intent intent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        intent.putExtra("state", airplaneModeOn);
        context.sendBroadcast(intent);
    }    
    
    public static boolean getWifiEnabled(Context context) {
        WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        return manager.isWifiEnabled();
    }

    public static void setWifiEnabled(Context context, boolean b) {
        WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        manager.setWifiEnabled(b);
    }

    public static boolean isScreenLocked(Context context) {
        KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        return keyguardManager.inKeyguardRestrictedInputMode();
    }
    
    public static String getCurrProcessName(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        String name = "";
        List<RunningAppProcessInfo> list = activityManager.getRunningAppProcesses();
        if (list != null) {
            int pid = android.os.Process.myPid();
            for (RunningAppProcessInfo info : list) {
                if (pid == info.pid) {
                    name = info.processName;
                    LogUtil.d(TAG, "current process name:" + name);
                    break;
                }
            }
        }
        return name;
    }
    
    public static String getStackTraceString(Throwable tr) {
        if (tr == null) {
            return "";
        }
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        tr.printStackTrace(pw);
        return sw.toString();
    }

    public static void _insertContact(ContentResolver resolver, String name,
            String phoneNumber, int typePhone,
            String email, int typeEmail) {
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                .build());
        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, name)
                .build());
        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, phoneNumber)
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, typePhone)
                .build());
        if (email != null) {
            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Email.DATA, email)
                    .withValue(ContactsContract.CommonDataKinds.Email.TYPE, typeEmail)
                    .build());
        }
        try {
            resolver.applyBatch(ContactsContract.AUTHORITY, ops);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (OperationApplicationException e) {
            e.printStackTrace();
        }
    }

    public static String byteCountToDisplaySize(long size) {
        String[] units = {"bytes","KB","MB","GB","TB","PB"};
        int unit = 0;
        long remain = 0;
        while (size > 1024 && unit < units.length - 1) {
            remain = size % 1024;
            size /= 1024;
            unit++;
        }
        return String.format(unit == 0 ? "%1$d %3$s" : "%2$.1f %3$s",
                size, size + remain / 1024.0f, units[unit]);
    }

}
