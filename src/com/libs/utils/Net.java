package com.libs.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnRouteParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

public class Net {
    private static final String TAG = Net.class.getSimpleName();
    
    public static final int EXCEPTION_FORWARD = 0;
    public static final int EXCEPTION_HTTP_POST = 10;
    public static final int EXCEPTION_HTTP_GET = 20;
    public static final int EXCEPTION_HTTP_STATUS = 30;
    public static final int EXCEPTION_NETWORK_UNAVAILABLE = 40;
    public static final int EXCEPTION_USER = 10000;
    
    private static final int BUFFER_DEFAULT_SIZE = 0x2000; //8K

    public static class NetType {
        public static final int NET_TYPE_UNAVAILABLE = 0;
        public static final int NET_TYPE_WIFI = 1;
        public static final int NET_TYPE_MOBILE_NET = 2;
        public static final int NET_TYPE_MOBILE_WAP = 3;
        
        private int type = NET_TYPE_UNAVAILABLE;
        private String proxy = null;
        private int port = 0;
        private String name = "unavailable";
        
        public int getType() {
            return type;
        }
        public void setType(int type) {
            this.type = type;
        }
        public String getProxy() {
            return proxy;
        }
        public void setProxy(String proxy) {
            this.proxy = proxy;
        }
        public int getPort() {
            return port;
        }
        public void setPort(int port) {
            this.port = port;
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        @Override
        public String toString() {
            return "NetType [" +
                    "type=" + type + "(" + name + ")" + ", " +
                    "proxy=" + proxy + ", " +
                    "port=" + port +
                    "]";
        }
    }
    
    public static boolean isNetworkAvailable(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) 
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        return info != null && info.isConnected();
    }

    public static NetType getNetworkType(Context context){
        NetType netType = new NetType();

        ConnectivityManager connectivityManager = (ConnectivityManager) 
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if(info == null || !info.isConnected())
            return netType;

        String type = info.getTypeName();
        if (type.equalsIgnoreCase("WIFI")) {
            netType.setType(NetType.NET_TYPE_WIFI);
            netType.setName(type);
        } else if(type.equalsIgnoreCase("MOBILE")) {
            // cmnet、cmwap、3gnet、3gwap
            netType.setType(NetType.NET_TYPE_MOBILE_NET);
            String extraInfo = info.getExtraInfo();
            if (extraInfo != null) {
                netType.setName(extraInfo);
            }
            String proxyHost = android.net.Proxy.getDefaultHost();
            if (!TextUtils.isEmpty(proxyHost)) {
                // WAP
                netType.setType(NetType.NET_TYPE_MOBILE_WAP);
                netType.setProxy(proxyHost);
                netType.setPort(android.net.Proxy.getDefaultPort());
            }
        }
        return netType;
    }

    
    
    // do not extends 'Exception' in order to distinguish Exception & MyException
    public static class MyException extends Throwable {
        private int id = 0;
        public int getID() {
            return id;
        }
        public MyException(int id, String msg) {
            super(msg);
            this.id = id;
        }
        public MyException(int id) {
            this.id = id;
        }
        public MyException(int id, Exception e) {
            super(e);
            this.id = id;
        }
        public MyException(Exception e) {
            super(e);
        }
        public MyException(MyException e) {
            super(e);
            this.id = e.id;
        }
        public MyException() {
        }
        @Override
        public String toString() {
            return super.toString() + "<" + id + ">";
        }
    }

    public static class Progress {
        private double mProgress = 0;
        private double mCurrMax;
        private double mCurrWeight;
        private int mPhase = 0;
        private ProgressListener mProgressListener = null;
        
        public Progress() {
        }
        
        public Progress(ProgressListener listener) {
            this();
            mProgressListener = listener;
        }

        public void setParamsForNextPhase(double max, double weight, int phase) {
            mCurrMax = max;
            mCurrWeight = weight;
            mPhase = phase;
        }
        
        public void setMax(double max) {
            mCurrMax = max;
        }
        
        // run into next phase, assuming params already set
        public void nextPhase() {
            nextPhase(mCurrMax, mCurrWeight, mPhase);
        }
        
        // set params for (max,weight) and run into next phase
        //  phase auto-incremented
        public void nextPhase(double max, double weight) {
            nextPhase(max, weight, mPhase + 1);
        }
        
        // set params for (max,weight,phase) and run into next phase
        public void nextPhase(double max, double weight, int phase) {
            mCurrMax = max;
            mCurrWeight = weight;
            mPhase = phase;
            if (mProgressListener != null)
                mProgressListener.onNextPhase(mCurrMax, mCurrWeight, mPhase);
        }

        public void publishProgress(double delta) {
            publishProgress(delta, null);
        }

        public void publishProgress(double delta, Object extra) {
            mProgress += mCurrWeight * (delta / mCurrMax);
            if (mProgressListener != null)
                mProgressListener.onProgress(mProgress, extra);
        }
        
        public void publishStart() {
            mProgress = 0.0;
            if (mProgressListener != null)
                mProgressListener.onStart();
        }

        public void publishFinished() {
            mProgress = 1.0;
            if (mProgressListener != null)
                mProgressListener.onFinished();
        }
        
        public interface ProgressListener {
            void onStart();
            void onProgress(double progress, Object extra);
            void onFinished();
            void onNextPhase(double max, double weight, int phase);
        }
        
        public static class DefaultProgressListener implements ProgressListener, Handler.Callback {
            private Handler mHandler = new Handler(this);
            private int mInterval = 1500;
            private double mInfoProgress = 0.0;
            private Object mInfoExtra = null;
            
            public DefaultProgressListener() {
            }
            
            public DefaultProgressListener(int msInterval) {
                mInterval = msInterval;
            }
            
            public int getInterval() {
                return mInterval;
            }

            public void setInterval(int interval) {
                this.mInterval = interval;
            }

            public void onStart() {
                mInfoProgress = 0.0;
                mHandler.sendEmptyMessage(MSG_START);
            }
            public void onProgress(double progress, Object extra) {
                mInfoProgress = progress;
                mInfoExtra = extra;
            }
            public void onFinished() {
                mHandler.sendEmptyMessage(MSG_FINISH);
            }
            public void onNextPhase(double max, double weight, int phase) {
                mHandler.obtainMessage(MSG_NEXT_PHASE, new Object[]{max, weight, phase}).sendToTarget();
            }
            
            private static final int MSG_START = 1;
            private static final int MSG_PROGRESS = 2;
            private static final int MSG_FINISH = 3;
            private static final int MSG_NEXT_PHASE = 4;
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                case MSG_START:
                    onStartUI();
                    mHandler.sendEmptyMessageDelayed(MSG_PROGRESS, mInterval);
                    break;
                case MSG_FINISH:
                    onFinishedUI();
                    mHandler.removeMessages(MSG_PROGRESS);
                    break;
                case MSG_PROGRESS: {
                    onProgressUI(mInfoProgress, mInfoExtra);
                    mHandler.sendEmptyMessageDelayed(MSG_PROGRESS, mInterval);
                    break;
                }
                case MSG_NEXT_PHASE: {
                    Object[] params = (Object[]) msg.obj;
                    Double max = (Double) params[0];
                    Double weight = (Double) params[1];
                    Integer phase = (Integer) params[2];
                    onNextPhaseUI(max, weight, phase);
                    break;
                }
                }
                return true;
            }
            
            public void onStartUI() {};
            public void onProgressUI(double progress, Object extra) {};
            public void onFinishedUI() {};
            public void onNextPhaseUI(double max, double weight, int phase) {};
        }
    }

    public static byte[] post(Context context, String url, 
            List<NameValuePair> params, Progress progress) throws MyException {
        ByteArrayOutputStream byteos = new ByteArrayOutputStream();
        post(context, url, params, byteos, progress);
        byte[] ret = byteos.toByteArray();
        try {
            byteos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }
    
    public static void post(Context context, String url,
            List<NameValuePair> params, OutputStream out, Progress progress)
            throws MyException {
        if (out == null) return;
        boolean progressStarted = false;
        try {
            NetType netType = getNetworkType(context);
            LogUtil.d(TAG, netType.toString());
            if (netType.getType() == NetType.NET_TYPE_UNAVAILABLE)
                throw new MyException(EXCEPTION_NETWORK_UNAVAILABLE, "Network is not available.");

            HttpClient httpclient = new DefaultHttpClient();
            HttpParams httpParameters = httpclient.getParams();
            HttpConnectionParams.setConnectionTimeout(httpParameters, 30000);
            HttpConnectionParams.setSoTimeout(httpParameters, 30000);
            if (netType.getType() == NetType.NET_TYPE_MOBILE_WAP) {
                HttpHost proxy = new HttpHost(netType.getProxy(), netType.getPort());
                httpParameters.setParameter(ConnRouteParams.DEFAULT_PROXY, proxy);
            }
            
            HttpPost httppost = new HttpPost(url);
            httppost.addHeader("Accept-Encoding", "gzip");
//          httppost.addHeader("Content-Type", "application/octet-stream");
            httppost.addHeader("Content-Type", "application/x-www-form-urlencoded;charset:UTF-8");
            httppost.addHeader("User-Agent", Utils.getDeviceInfoForUserAgent(context));

            if (params != null)
                httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            HttpResponse response = httpclient.execute(httppost);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                int total = 0;
                if (progress != null) {
                    Header[] headers = response.getHeaders("Content-Length");
                    if (headers == null || headers.length == 0) {
                        //throw new MyException(EXCEPTION_HTTP_POST, "Response need 'Content-Length'.");
                    }
                    else {
                        total = Integer.parseInt(headers[0].getValue());
                    }
                }
                if (total > 0 && progress != null) {
                    progressStarted = true;
                    progress.setMax(total);
                    progress.publishStart();
                    progress.nextPhase();
                }

                HttpEntity entity = response.getEntity();
                InputStream is = entity.getContent();
                Header zipHeader = entity.getContentEncoding();
                if (zipHeader != null && zipHeader.getValue().indexOf("gzip") >= 0)
                    is = new GZIPInputStream(is);

                final int SIZE = BUFFER_DEFAULT_SIZE;
                byte[] buffer = new byte[SIZE];
                int count = 0;
                while ((count = is.read(buffer)) > 0) {
                    out.write(buffer, 0, count);
                    if (progressStarted) {
                        progress.publishProgress((double)count);
                    }
                }
            } else {
                throw new MyException(EXCEPTION_HTTP_STATUS, "post statusCode:" +
                        response.getStatusLine().getStatusCode());
            }
            httpclient.getConnectionManager().shutdown();
        } catch (Exception e) {
            throw new MyException(EXCEPTION_HTTP_POST, e);
        } finally {
//            if (out != null)
//                try { out.close(); } catch (IOException e) { e.printStackTrace(); }
            if (progressStarted)
                progress.publishFinished();
        }
    }

    public static byte[] get(Context context, String url, Progress progress) throws MyException {
        ByteArrayOutputStream byteos = new ByteArrayOutputStream();
        get(context, url, byteos, progress);
        byte[] ret = byteos.toByteArray();
        try {
            byteos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public static void get(Context context, String url, 
            OutputStream out, Progress progress) throws MyException {
        if (out == null) return;
        boolean progressStarted = false;
        try {
            NetType netType = getNetworkType(context);
            LogUtil.d(TAG, netType.toString());
            if (netType.getType() == NetType.NET_TYPE_UNAVAILABLE)
                throw new MyException(EXCEPTION_NETWORK_UNAVAILABLE, "Network is not available.");

            HttpClient httpclient = new DefaultHttpClient();
            HttpParams httpParameters = httpclient.getParams();
            HttpConnectionParams.setConnectionTimeout(httpParameters, 30000);
            HttpConnectionParams.setSoTimeout(httpParameters, 30000);
            if (netType.getType() == NetType.NET_TYPE_MOBILE_WAP) {
                HttpHost proxy = new HttpHost(netType.getProxy(), netType.getPort());
                httpParameters.setParameter(ConnRouteParams.DEFAULT_PROXY, proxy);
            }
            
            HttpGet httpget = new HttpGet(url);
            httpget.addHeader("Accept-Encoding", "gzip");
            httpget.addHeader("User-Agent", Utils.getDeviceInfoForUserAgent(context));

            HttpResponse response = httpclient.execute(httpget);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                int total = 0;
                if (progress != null) {
                    Header[] headers = response.getHeaders("Content-Length");
                    if (headers == null || headers.length == 0) {
                        //throw new MyException(EXCEPTION_HTTP_GET, "Response need 'Content-Length'.");
                    }
                    else {
                        total = Integer.parseInt(headers[0].getValue());
                    }
                }

                if (total > 0 && progress != null) {
                    progressStarted = true;
                    progress.setMax(total);
                    progress.publishStart();
                    progress.nextPhase();
                }

                HttpEntity entity = response.getEntity();
                InputStream is = entity.getContent();
                Header zipHeader = entity.getContentEncoding();
                if (zipHeader != null && zipHeader.getValue().indexOf("gzip") >= 0)
                    is = new GZIPInputStream(is);

                final int SIZE = BUFFER_DEFAULT_SIZE;
                byte[] buffer = new byte[SIZE];
                int count = 0;
                while ((count = is.read(buffer)) > 0) {
                    out.write(buffer, 0, count);
                    if (progressStarted) {
                        progress.publishProgress((double)count);
                    }
                }
            } else {
                throw new MyException(EXCEPTION_HTTP_STATUS, "get statusCode:" +
                        response.getStatusLine().getStatusCode());
            }
            httpclient.getConnectionManager().shutdown();
        } catch (Exception e) {
            throw new MyException(EXCEPTION_HTTP_GET, e);
        } finally {
//            if (out != null)
//                try { out.close(); } catch (IOException e) { e.printStackTrace(); }
            if (progressStarted)
                progress.publishFinished();
        }
    }

}
