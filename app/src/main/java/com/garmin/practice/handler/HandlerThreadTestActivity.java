package com.garmin.practice.handler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.garmin.practice.R;


import java.lang.ref.WeakReference;

public class HandlerThreadTestActivity extends AppCompatActivity {

    Button mBtnStartDownload;

    private Handler mUIHandler;
    private DownloadHandlerThread mDownloadThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handler_thread_test);
        initDownThread();
        initButton();
    }

    private void initButton() {
        mBtnStartDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDownloadThread.start();
                mDownloadThread.startDownload();
                mBtnStartDownload.setText("正在下载");
                mBtnStartDownload.setEnabled(false);
            }
        });
    }

    private void initDownThread() {
        mUIHandler = new UIHandler(this);
        mDownloadThread = new DownloadHandlerThread("下载线程");
        mDownloadThread.setUIHandler(mUIHandler);
        mDownloadThread.setDownloadUrls("www.91porn.com",
                "www.baidu.com", "www.google.com");
    }

    static class UIHandler extends Handler {
        private WeakReference<Activity> mActivity;
        TextView mTvStartMsg;
        TextView mTvFinishMsg;

        public UIHandler(Activity activity) {
            mActivity = new WeakReference<>(activity);
            mTvStartMsg = activity.findViewById(R.id.tv_start_msg);
            mTvFinishMsg = activity.findViewById(R.id.tv_finish_msg);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case DownloadHandlerThread.START_DOWNLOAD:
                    mTvStartMsg.setText(msg.obj.toString());
                    break;
                case DownloadHandlerThread.FINISH_DOWNLOAD:
                    mTvFinishMsg.setText(msg.obj.toString());
                    break;
                default:
                    break;
            }
        }
    }
}