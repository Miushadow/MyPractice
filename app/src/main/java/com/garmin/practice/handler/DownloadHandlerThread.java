package com.garmin.practice.handler;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;

import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;

public class DownloadHandlerThread extends HandlerThread {

    private final String TAG = this.getClass().getSimpleName();
    private final String KEY_URL = "url";
    public static final int START_DOWNLOAD = 1;
    public static final int FINISH_DOWNLOAD = 2;
    private List<String> mDownloadUrlList;

    private Handler mWorkHandler; //用于子线程传递消息，执行任务的Handler
    private Handler mUIHandler; //外部传入，在主线程显示下载状态的Handler

    /**
     * 默认构造方法，随便传入一个字符串作为这个HandlerThread的名称
     * @param name
     */
    public DownloadHandlerThread(String name) {
        super(name);
    }

    @Override
    protected void onLooperPrepared() {
        super.onLooperPrepared();
        mWorkHandler = new WorkHandler();
        if (mUIHandler == null) {
            throw new IllegalArgumentException("UIHandler has not been inject");
        }
    }

    public void setUIHandler(final Handler UIHandler) {
        mUIHandler = UIHandler;
    }

    public Handler getUIHandler() {
        return mUIHandler;
    }

    public void setDownloadUrls(String... urls) {
        mDownloadUrlList = Arrays.asList(urls);
    }

    public void startDownload() {
        for (String url : mDownloadUrlList) {
            Message msg = mWorkHandler.obtainMessage();
            Bundle bundle = new Bundle();
            bundle.putString(KEY_URL, url);
            msg.setData(bundle);
            mWorkHandler.sendMessage(msg);
        }
    }

    class WorkHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.getData() == null || msg.getData().getString(KEY_URL) == null) {
                Log.e(TAG, "DOWNLOAD URL is null");
                return;
            }
            String url = msg.getData().getString(KEY_URL);

            //下载开始，通知主线程更新UI
            Message startMsg = mUIHandler.obtainMessage();
            startMsg.what = START_DOWNLOAD;
            startMsg.obj = "开始下载 : " + url;
            mUIHandler.sendMessage(startMsg);

            //模拟下载
            SystemClock.sleep(4000);

            //下载完成，通知主线程更新UI
            Message finishMsg = mUIHandler.obtainMessage();
            finishMsg.what = FINISH_DOWNLOAD;
            finishMsg.obj = "下载完成 : " + url;
            mUIHandler.sendMessage(finishMsg);
        }
    }
}
