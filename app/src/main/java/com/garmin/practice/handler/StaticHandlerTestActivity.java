
package com.garmin.practice.handler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.MessageQueue;
import android.widget.TextView;

import com.garmin.practice.R;

import java.lang.ref.WeakReference;

/**
 * Handler正确写法（静态内部类+弱引用）的测试类，通过这种写法，可以避免Handler引起的内存泄漏问题
 */
public class StaticHandlerTestActivity extends AppCompatActivity {

    StaticHandler mSendMessageHandler;
    StaticHandler mPostMessageHandler;
    TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_static_handler_test);
        sendMessageTest();
        postMessageTest();
    }

    /**
     * 正确写法的Handler，通过使用静态内部类定义+弱引用的方式，避免内存泄漏
     */
    //自定义Handler的子类（静态内部类），并复写handleMessage()方法，handleMessage()中可以获取相关View组件,
    //并定义在收到对应消息后，具体要在UI中做什么事情
    private static class StaticHandler extends Handler {

        //定义弱引用实例，弱引用用于描述非必需对象，如果一个对象只有弱引用指向它，那么当GC发生时，无论内存是否
        //充足，该对象都会被回收。在Handler中利用弱引用来持有Activity对象的原因是，当Activity未退出时，会有
        //其它的强引用指向它，这时Handler照常工作。而当Activity退出后，如果Handler仍在工作，但Handler中是通
        //过弱引用持有Activity的，所以下一次GC时，会把Activity回收
        private WeakReference<Activity> mActivity;
        TextView mTextView;

        //在构造方法中传入需持有的Activity实例
        public StaticHandler(Activity activity) {
            //使用弱引用持有Activity实例
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            Activity activity = mActivity.get();
            //根据不同线程发送过来的消息，执行不同的UI操作
            if (activity != null) {
                mTextView  = activity.findViewById(R.id.tv_test_handler_sendmessage);
            }
            switch (msg.what) {
                case 1:
                    if (mTextView != null) {
                        mTextView.setText("通过" + msg.obj + "方式更新UI");
                    }
                case 2:
                    if (mTextView != null) {
                        mTextView.setText("通过" + msg.obj + "方式更新UI");
                    }
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 通过Handler.sendMessage()的方式发送消息
     * sendMessage需要通过实现handleMessage这个回调函数来实现对消息的处理，一般用于多判断条件的场景
     */
    private void sendMessageTest() {
        //#步骤1：在主线程中创建Handler实例（Handler实例必须定义在主线程中）
        mSendMessageHandler = new StaticHandler(this);

        //开启工作线程，采用new Thread类实现多线程演示
        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //#步骤2：通过Message.obtain()获取Message池中的消息对象，并填充消息内容
                Message msg_a = Message.obtain();
                msg_a.what = 1; //消息标识
                msg_a.obj = "sendMessage"; //消息内容存放，可在另一端读取
                //#步骤3：在工作线程中，通过Handler对象将消息发送到消息队列中（总结来说，Handler对象在主线程
                //中定义，在工作线程中通过sendMessage(Message msg)发送消息
                mSendMessageHandler.sendMessage(msg_a);

                //通过SendMessageDelayed发送延时消息
                Message msg_b = Message.obtain();
                msg_b.what = 2;
                msg_b.obj = "sendMessageDelayed";
                mSendMessageHandler.sendMessageDelayed(msg_b, 3000);
            }
        }.start();
    }

    /**
     * 通过Handler.post()的方式发送消息
     * post与sendMessage本质上并无区别，从源码来看，最终都是调用Handler中的sendMessageDelayed方法，不同的
     * 是post在getPostMessage方法中，通过Message.obtain获取一个消息对象，并将Runnable对象赋给Message对象
     * 的callback
     * post一般用于单个场景，比如单一的倒计时弹框功能
     */
    private void postMessageTest() {
        mTextView = findViewById(R.id.tv_test_handler_postmessage);
        mPostMessageHandler = new StaticHandler(this);

        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(9000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //#步骤1：直接调用Handler的post方法，传入一个Runnable对象，需要UI线程做的事情直接写在
                //Runnable对象的run方法中
                mPostMessageHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mTextView.setText("通过post方式更新UI");
                    }
                });

                //通过postDelay发送延时消息
                mPostMessageHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mTextView.setText("通过postDelayed方式更新UI");
                    }
                }, 3000);
            }
        }.start();
    }

}