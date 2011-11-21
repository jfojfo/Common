package com.libs.utils;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

public class Notify {
    
    public static void cancel(Context context, int id) {
        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(id);
    }
    
    public static void notify(Context context, Notification notification, int id) {
        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationManager.cancel(id);
        notificationManager.notify(id, notification);
    }
    
    public static void notify(Context context, Notification notification) {
        notify(context, notification, 0);
    }
    
    public static void notify(Context context, CharSequence title, CharSequence msg,
            Uri soundUri, PendingIntent pendingIntent, int iconResId, int id) {
        if (iconResId == 0)
            iconResId = Utils.getIconResId(context);
        if (title == null)
            title = Utils.getLabel(context);
        if (pendingIntent == null)
            pendingIntent = PendingIntent.getActivity(context, 0, new Intent(), PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new Notification();
        notification.icon = iconResId;
        notification.tickerText = msg;
        notification.when = System.currentTimeMillis();
        notification.contentIntent = pendingIntent;
        notification.setLatestEventInfo(context, title, msg, pendingIntent);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        if (soundUri == null)
            notification.defaults = Notification.DEFAULT_SOUND;
        else
            notification.sound = soundUri;

        notify(context, notification, id);
    }

    public static PendingIntent getPendingIntent(Context context, Class<?> clazz) {
        PendingIntent pendingIntent = null;
        if (clazz != null) {
            Intent notificationIntent = new Intent(context, clazz);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            if (Activity.class.isAssignableFrom(clazz)) {
                pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            } else if (Service.class.isAssignableFrom(clazz)) {
                pendingIntent = PendingIntent.getService(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            } else if (BroadcastReceiver.class.isAssignableFrom(clazz)) {
                pendingIntent = PendingIntent.getBroadcast(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            }
        }
        return pendingIntent;
    }
    
    public static void notify(Context context, CharSequence title, CharSequence msg,
            Uri soundUri, Class<?> clazz, int iconResId, int id) {
        PendingIntent pendingIntent = getPendingIntent(context, clazz);
        notify(context, title, msg, soundUri, pendingIntent, iconResId, id);
    }
    
    public static void notify(Context context, CharSequence title, CharSequence msg, Uri soundUri, Class<?> clazz, int id) {
        int iconResId = Utils.getIconResId(context);
        notify(context, title, msg, soundUri, clazz, iconResId, id);
    }
    
    public static void notify(Context context, CharSequence title, CharSequence msg, Uri soundUri, Class<?> clazz) {
        notify(context, title, msg, soundUri, clazz, 0);
    }
    
    public static void notify(Context context, CharSequence title, CharSequence msg, Class<?> clazz) {
        notify(context, title, msg, null, clazz);
    }
    
    public static void notify(Context context, CharSequence title, CharSequence msg, Uri soundUri) {
        notify(context, title, msg, soundUri, null);
    }
    
    public static void notify(Context context, CharSequence title, CharSequence msg) {
        notify(context, title, msg, (Uri) null);
    }

    public static void notify(Context context, CharSequence msg, Uri soundUri, Class<?> clazz, int id) {
        CharSequence label = Utils.getLabel(context);
        notify(context, label, msg, soundUri, clazz, id);
    }
    
    public static void notify(Context context, CharSequence msg, Uri soundUri, Class<?> clazz) {
        notify(context, msg, soundUri, clazz, 0);
    }
    
    public static void notify(Context context, CharSequence msg, Class<?> clazz) {
        notify(context, msg, (Uri) null, clazz);
    }
    
    public static void notify(Context context, CharSequence msg, Uri soundUri) {
        notify(context, msg, soundUri, null);
    }
    
    public static void notify(Context context, CharSequence msg) {
        notify(context, msg, null, null, 0);
    }

    public static void notify(Context context, RemoteViews contentView, Uri soundUri, PendingIntent pendingIntent, int flag, int id) {
        if (pendingIntent == null)
            pendingIntent = PendingIntent.getActivity(context, 0, new Intent(), PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new Notification();
        notification.icon = Utils.getIconResId(context);
        notification.contentView = contentView;
        notification.contentIntent = pendingIntent;
        notification.flags |= flag;
        if (soundUri == null)
            notification.defaults = Notification.DEFAULT_SOUND;
        else
            notification.sound = soundUri;
        notify(context, notification, id);
    }

    public static void notify(Context context, RemoteViews contentView, PendingIntent pendingIntent) {
        notify(context, contentView, null, pendingIntent, 0, 0);
    }

    public static void notify(Context context, RemoteViews contentView, Uri soundUri, Class<?> clazz) {
        PendingIntent pendingIntent = getPendingIntent(context, clazz);
        notify(context, contentView, soundUri, pendingIntent, 0, 0);
    }

    public static void notify(Context context, RemoteViews contentView, Class<?> clazz) {
        notify(context, contentView, null, clazz);
    }
    
    public static void notify(Context context, RemoteViews contentView, Uri soundUri) {
        notify(context, contentView, soundUri, null);
    }
    
    public static void notify(Context context, RemoteViews contentView) {
        notify(context, contentView, (Uri) null);
    }
    
    
    public static Notification getNotification(Context context, RemoteViews contentView,
            Uri soundUri, PendingIntent pendingIntent, int flag) {
        if (pendingIntent == null)
            pendingIntent = PendingIntent.getActivity(context, 0, new Intent(), PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new Notification();
        notification.icon = Utils.getIconResId(context);
        notification.contentView = contentView;
        notification.contentIntent = pendingIntent;
        notification.flags |= flag;
        if (soundUri == null)
            notification.defaults = Notification.DEFAULT_SOUND;
        else
            notification.sound = soundUri;
        return notification;
    }
    
    public static Notification getNotification(Context context, RemoteViews contentView,
            Uri soundUri, Class<?> clazz, int flag) {
        PendingIntent pendingIntent = getPendingIntent(context, clazz);
        return getNotification(context, contentView, soundUri, pendingIntent, flag);
    }
    
    public static Notification getNotification(Context context, RemoteViews contentView,
            Uri soundUri, int flag) {
        return getNotification(context, contentView, soundUri, (Class<?>) null, flag);
    }
    
}
