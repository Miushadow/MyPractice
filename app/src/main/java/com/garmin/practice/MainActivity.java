package com.garmin.practice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.garmin.practice.handler.HandlerThreadTestActivity;
import com.garmin.practice.handler.StaticHandlerTestActivity;
import com.garmin.practice.handler.WrongHandlerTestActivity;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity {

    Button btn_static_handler_test;
    Button btn_wrong_handler_test;
    Button btn_handler_thread_test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initTestCases();
    }

    private void initTestCases() {
        initStaticHandlerTest();
        initWrongHandlerTest();
        initHandlerThreadTest();
    }

    private void initHandlerThreadTest() {
        btn_handler_thread_test = findViewById(R.id.btn_handler_thread_test);
        btn_handler_thread_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(MainActivity.this, HandlerThreadTestActivity.class);
                startActivity(mIntent);
            }
        });
    }

    private void initStaticHandlerTest() {
        btn_static_handler_test = findViewById(R.id.btn_static_handler_test);
        btn_static_handler_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(MainActivity.this, StaticHandlerTestActivity.class);
                startActivity(mIntent);
            }
        });
    }

    private void initWrongHandlerTest() {
        btn_wrong_handler_test = findViewById(R.id.btn_wrong_handler_test);
        btn_wrong_handler_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(MainActivity.this, WrongHandlerTestActivity.class);
                startActivity(mIntent);
            }
        });
    }


}