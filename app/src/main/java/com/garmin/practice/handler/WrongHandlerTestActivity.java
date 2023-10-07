package com.garmin.practice.handler;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.MessageQueue;
import android.widget.TextView;

import com.garmin.practice.R;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class WrongHandlerTestActivity extends AppCompatActivity {

    public TextView tv_wrong_handler;
    WrongHandler mWrongHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wrong_handler_test);
        tv_wrong_handler = findViewById(R.id.tv_test_wrong_handler);
        wrongHandlerTest();
    }

    private void wrongHandlerTest() {
        mWrongHandler = new WrongHandler();
        new Thread() {
            @Override
            public void run() {
                Message msg = Message.obtain();
                msg.what = 1;
                msg.obj = "A";
                mWrongHandler.sendMessageDelayed(msg, 8000);
            }
        }.start();
    }


    /**
     * 错误的Handler定义方法，由于WrongHandler是非静态内部类，会隐式持有外部Activity引用。当Activity
     * 退出时，由于该Handler发送的延迟消息还未处理完毕，而Activity对象又被Handler所持有，所以会导致Activity
     * 无法被正常回收
     */
    class WrongHandler extends Handler {

        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 1:
                    tv_wrong_handler.setText("错误写法Handler测试线程更新UI");
                    break;
                default:
                    break;
            }
        }
    }
}