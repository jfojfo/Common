package com.libs.test;

import android.app.Activity;
import android.os.Bundle;

import com.google.gson.Gson;
import com.libs.utils.LogUtils;
import com.libs.utils.Net;
import com.libs.utils.Net.NetType;
import com.libs.utils.Notify;
import com.libs.utils.StringConverter;
import com.libs.utils.Utils;

import java.io.IOException;


public class TestActivity extends Activity {
    private static final String TAG = TestActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        test();
    }
    
    private void test() {
        LogUtils.e("================= start ==================");
        try {
            LogUtils.e("isNetworkAvailable:" + Net.isNetworkAvailable(this));
            NetType type = Net.getNetworkType(this);
            LogUtils.e(type.toString());
            
            LogUtils.e("getDeviceDensity:" + Utils.getDeviceDensity(this));
            LogUtils.e("getLocale:" + Utils.getLocale(this));
            LogUtils.e("getScreenRect:" + Utils.getScreenRect(this));
            LogUtils.e("isSDCardMounted:" + Utils.isSDCardMounted());
            LogUtils.e("getIMEI:" + Utils.getIMEI(this));
            LogUtils.e("getMacAddress:" + Utils.getMacAddress(this));
            LogUtils.e("getDeviceId:" + Utils.getDeviceId(this));
            LogUtils.e("getSimId:" + Utils.getSimId(this));
            LogUtils.e("getVersionCode:" + Utils.getVersionCode(this));
            LogUtils.e("getVersionName:" + Utils.getVersionName(this));
            LogUtils.e("isPackageExists:" + Utils.isPackageExists(this, "com.adobe.flashplayer"));
//            LogUtils.e("formatTimestamp2Date:" + StringConverter.formatTimestamp2Date(System.currentTimeMillis()));
            LogUtils.e("parseDateTimeToMillis:" + StringConverter.parseDateTimeToMillis("20111111 090647"));
            LogUtils.e("parseEmailDateTimeToMillis:" + StringConverter.parseEmailDateTimeToMillis("2011-11-11 09:06:48"));

            Gson gson = new Gson();
            String hex = "1234567890ABCDEFFF";
            byte[] bytes = StringConverter.hexToByte(hex);
            LogUtils.e("hexToByte:" + gson.toJson(bytes));
            LogUtils.e("byteToHex:" + StringConverter.byteToHex(bytes));
            byte[] ascii = StringConverter.toAscii("FE12CD34AB567890");
            LogUtils.e("toAscii:" + gson.toJson(ascii));
            LogUtils.e("fromAscii:" + StringConverter.fromAscii(ascii));
            
            Notify.notify(this, "message");
            
            LogUtils.e("isScreenLocked:" + Utils.isScreenLocked(this));
        } catch (IOException e) {
            e.printStackTrace();
        }
        LogUtils.e(":");
        LogUtils.e("================== end ==================\n\n");
    }

}
