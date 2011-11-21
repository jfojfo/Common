package com.libs.statistics;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.waps.AdView;
import com.waps.AppConnect;

public class Ads {

    public static void initAdsView(Activity activity, View view) {
        String ads = Stat.getConfigParams(activity, "ads");
        if ("waps".equalsIgnoreCase(ads)) {
            // www.waps.cn
            if (view instanceof LinearLayout) {
                LinearLayout container =(LinearLayout) view;
                new AdView(activity, container).DisplayAd(21); //每20秒轮换一次广告；最少为20
            }
        }
    }

    public static void init(Context context) {
        String ads = Stat.getConfigParams(context, "ads");
        if ("waps".equalsIgnoreCase(ads)) {
            AppConnect.getInstance(context);
            AppConnect.getInstance(context).getPushAd();
        }
    }

    public static void finit(Context context) {
        String ads = Stat.getConfigParams(context, "ads");
        if ("waps".equalsIgnoreCase(ads)) {
            AppConnect.getInstance(context).finalize();
        }
    }

}
