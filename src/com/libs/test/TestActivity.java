package com.libs.test;

import java.io.IOException;

import android.app.Activity;
import android.os.Bundle;

import com.google.gson.Gson;
import com.libs.utils.LogUtil;
import com.libs.utils.Net;
import com.libs.utils.Net.NetType;
import com.libs.utils.Notify;
import com.libs.utils.StringConverter;
import com.libs.utils.Utils;


public class TestActivity extends Activity {
    private static final String TAG = TestActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        test();
    }
    
    private void test() {
        LogUtil.e(TAG, "================= start ==================");
        try {
            LogUtil.e(TAG, "isNetworkAvailable:" + Net.isNetworkAvailable(this));
            NetType type = Net.getNetworkType(this);
            LogUtil.e(TAG, type.toString());
            
            LogUtil.e(TAG, "getDeviceDensity:" + Utils.getDeviceDensity(this));
            LogUtil.e(TAG, "getLocale:" + Utils.getLocale(this));
            LogUtil.e(TAG, "getScreenRect:" + Utils.getScreenRect(this));
            LogUtil.e(TAG, "isSDCardMounted:" + Utils.isSDCardMounted());
            LogUtil.e(TAG, "getIMEI:" + Utils.getIMEI(this));
            LogUtil.e(TAG, "getMacAddress:" + Utils.getMacAddress(this));
            LogUtil.e(TAG, "getDeviceId:" + Utils.getDeviceId(this));
            LogUtil.e(TAG, "getSimId:" + Utils.getSimId(this));
            LogUtil.e(TAG, "getVersionCode:" + Utils.getVersionCode(this));
            LogUtil.e(TAG, "getVersionName:" + Utils.getVersionName(this));
            LogUtil.e(TAG, "isPackageExists:" + Utils.isPackageExists(this, "com.adobe.flashplayer"));
//            LogUtil.e(TAG, "formatTimestamp2Date:" + StringConverter.formatTimestamp2Date(System.currentTimeMillis()));
            LogUtil.e(TAG, "parseDateTimeToMillis:" + StringConverter.parseDateTimeToMillis("20111111 090647"));
            LogUtil.e(TAG, "parseEmailDateTimeToMillis:" + StringConverter.parseEmailDateTimeToMillis("2011-11-11 09:06:48"));

            Gson gson = new Gson();
            String hex = "1234567890ABCDEFFF";
            byte[] bytes = StringConverter.hexToByte(hex);
            LogUtil.e(TAG, "hexToByte:" + gson.toJson(bytes));
            LogUtil.e(TAG, "byteToHex:" + StringConverter.byteToHex(bytes));
            byte[] ascii = StringConverter.toAscii("FE12CD34AB567890");
            LogUtil.e(TAG, "toAscii:" + gson.toJson(ascii));
            LogUtil.e(TAG, "fromAscii:" + StringConverter.fromAscii(ascii));
            
            Notify.notify(this, "message");
            
            LogUtil.e(TAG, "isScreenLocked:" + Utils.isScreenLocked(this));
        } catch (IOException e) {
            e.printStackTrace();
        }
        LogUtil.e(TAG, ":");
        LogUtil.e(TAG, "================== end ==================\n\n");
    }

}
