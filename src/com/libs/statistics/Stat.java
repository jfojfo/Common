package com.libs.statistics;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;

import com.mobclick.android.MobclickAgent;

public class Stat {

    public static void onResume(Context context) {
        MobclickAgent.onResume(context);
    }

    public static void onPause(Context context) {
        MobclickAgent.onPause(context);
    }
    
    public static void onError(Context context) {
        MobclickAgent.onError(context);
    }
    
    public static void update(Context context) {
        MobclickAgent.update(context);
    }
    
    public static void updateOnlineConfig(Context context) {
        MobclickAgent.updateOnlineConfig(context);
    }
    
    public static String getConfigParams(Context context, String key) {
        return MobclickAgent.getConfigParams(context, key);
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
    
}
