package com.libs.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;

import com.libs.utils.LogUtil;

public class CommonService extends Service {
    private static final String TAG = CommonService.class.getSimpleName();
    public static final String ACTION_STOP_SERVICE = CommonService.class.getPackage().getName() + ".ACTION_STOP_SERVICE";
    public static final String ACTION_UPDATE_STATISTICS = CommonService.class.getPackage().getName() + ".ACTION_UPDATE_STATISTICS";
    public static final String ACTION_DOWNLOAD = CommonService.class.getPackage().getName() + ".ACTION_DOWNLOAD";
    public static final String EXTRA_PARAM_DOWNLOAD_ID = "id";
    public static final String EXTRA_PARAM_DOWNLOAD_OUTPUT = "output";
    
    public static void startService(Context context) {
        Intent service = new Intent(context, CommonService.class);
        context.startService(service);
    }
    
    public static void stopService(Context context) {
        Intent service = new Intent(context, CommonService.class);
        service.setAction(ACTION_STOP_SERVICE);
        context.startService(service);
    }
    
    public static void updateStatistics(Context context) {
        Intent service = new Intent(context, CommonService.class);
        service.setAction(ACTION_UPDATE_STATISTICS);
        context.startService(service);
    }
    
    public static void download(Context context, int id, String url, String outPath) {
        Intent service = new Intent(context, CommonService.class);
        service.setAction(ACTION_DOWNLOAD);
        service.setData(Uri.parse(url));
        service.putExtra(EXTRA_PARAM_DOWNLOAD_ID, id);
        service.putExtra(EXTRA_PARAM_DOWNLOAD_OUTPUT, outPath);
        context.startService(service);
    }
    
    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.d(TAG, "service created.");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtil.d(TAG, "Received start id " + startId + ": " + intent);
        if (intent != null) {
            String action = intent.getAction();
            LogUtil.d(TAG, "onStartCommand:" + action);
            if (ACTION_UPDATE_STATISTICS.equals(action)) {
                handleUpdateStatistics();
            } else if (ACTION_STOP_SERVICE.equals(action)) {
                stopSelf();
            } else if (ACTION_DOWNLOAD.equals(action)) {
                int id = intent.getIntExtra(EXTRA_PARAM_DOWNLOAD_ID, 0);
                String output = intent.getStringExtra(EXTRA_PARAM_DOWNLOAD_OUTPUT);
                String url = intent.getDataString();
                handleDownload(id, url, output);
            }
        }
        
        return START_STICKY;
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.d(TAG, "service destroyed.");
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    private void handleUpdateStatistics() {
//        final Context context = CommonService.this;
//        new Thread() {
//            @Override
//            public void run() {
//                setName("update-statistics-thread");
//                setPriority(Process.THREAD_PRIORITY_BACKGROUND);
//
//                Stat.update(context);
//                Stat.updateOnlineConfig(context);
//                LogUtil.d(TAG, "ads:" + Stat.getConfigParams(context, "ads"));
//                LogUtil.d(TAG, "channel:" + Stat.getMetaDataInManifest(context, "UMENG_CHANNEL"));
//            }
//        }.start();
    }

    private void handleDownload(int id, String url, String output) {
    }

}
