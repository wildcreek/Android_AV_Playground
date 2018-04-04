package com.wildcreek.playground;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.wildcreek.playground.task1.DrawImageActivity;
import com.wildcreek.playground.task2.AudioRecordActivity;
import com.wildcreek.playground.task3.CaptureCamera1Activity;
import com.wildcreek.playground.task4.MediaExtractorMuxerActivity;
import com.wildcreek.playground.task7.AudioEncoderDecoderActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView task1 ,task2,task3 ,task4 ,task5 , task6 , task7 , task8 , task9 , task10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    private void initViews() {
        task1 = findViewById(R.id.tv_task1);
        task2 = findViewById(R.id.tv_task2);
        task3 = findViewById(R.id.tv_task3);
        task4 = findViewById(R.id.tv_task4);
        task7 = findViewById(R.id.tv_task7);
        task1.setOnClickListener(this);
        task2.setOnClickListener(this);
        task3.setOnClickListener(this);
        task4.setOnClickListener(this);
        task7.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.tv_task1:
                startActivity(DrawImageActivity.class);
                break;
            case R.id.tv_task2:
                startActivity(AudioRecordActivity.class);
                break;
            case R.id.tv_task3:
                startActivity(CaptureCamera1Activity.class);
                break;
            case R.id.tv_task4:
                startActivity(MediaExtractorMuxerActivity.class);
                break;
            case R.id.tv_task7:
                startActivity(AudioEncoderDecoderActivity.class);
                break;
            default:
                break;
        }
    }
    private void startActivity(Class activity){
        Intent intent = new Intent(this , activity);
        startActivity(intent);
    }
}
