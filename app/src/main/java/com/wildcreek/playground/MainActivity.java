package com.wildcreek.playground;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.wildcreek.playground.task4.MediaExtractorMuxerActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(this , MediaExtractorMuxerActivity.class);
        startActivity(intent);
    }
}
