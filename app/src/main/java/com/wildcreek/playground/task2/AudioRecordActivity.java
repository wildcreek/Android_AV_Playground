package com.wildcreek.playground.task2;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.wildcreek.playground.R;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AudioRecordActivity extends AppCompatActivity implements View.OnClickListener {

    private Button startRecord;
    private Button stopRecord;
    private Button startPlay;
    private Button stopPlay;
    private TextView status;
    private AudioRecord audioRecord;
    private AudioTrack audioTrack;
    private boolean isRecording;
    private boolean isPlaying;
    private File audioFile;
    private ExecutorService threadPool  = Executors.newSingleThreadExecutor();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_record);
        startRecord = findViewById(R.id.btn_start_record);
        stopRecord = findViewById(R.id.btn_stop_record);
        startPlay = findViewById(R.id.btn_start_play);
        stopPlay = findViewById(R.id.btn_stop_play);
        status = findViewById(R.id.tv_status);
        startRecord.setOnClickListener(this);
        stopRecord.setOnClickListener(this);
        startPlay.setOnClickListener(this);
        stopPlay.setOnClickListener(this);

        File audioPath = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/audio/");
        audioPath.mkdirs();
        audioFile = new File(audioPath, "test.pcm");

        //PCM:Pulse Code Modulation
        int sampleRateInHz = 44100;
        int channelConfig = AudioFormat.CHANNEL_IN_STEREO;
        int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
        int bufferSize = AudioTrack.getMinBufferSize(sampleRateInHz, channelConfig, audioFormat);
        Log.i("AudioRecordActivity", "bufferSize " + bufferSize);
        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, sampleRateInHz, channelConfig
                , audioFormat, bufferSize * 4);

        audioTrack = new AudioTrack(AudioManager.STREAM_VOICE_CALL, sampleRateInHz, channelConfig,
                audioFormat, bufferSize * 4, AudioTrack.MODE_STREAM);

        notifyStatus();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start_record:
                if(!isRecording && !isPlaying ){
                    threadPool.execute(new Runnable() {
                        @Override
                        public void run() {
                            startRecord();
                        }
                    });
                }
                break;
            case R.id.btn_stop_record:
                stopRecord();
                break;
            case R.id.btn_start_play:
                if (!isPlaying && !isRecording) {
                    threadPool.execute(new Runnable() {
                        @Override
                        public void run() {
                            startPlay();
                        }
                    });
                }
                break;
            case R.id.btn_stop_play:
                stopPlay();
                break;
        }
    }

    private void startPlay() {
        audioTrack.play();
        isPlaying = true;
        notifyStatus();
        byte[] audioData = new byte[4096];
        DataInputStream dis;
        try {
            dis = new DataInputStream(new BufferedInputStream(new FileInputStream(audioFile)));
            while (isPlaying && dis.available() > 0) {
                int i = 0;
                while (dis.available() > 0 && i < audioData.length) {
                    audioData[i] = dis.readByte();
                    i++;
                }
                audioTrack.write(audioData, 0, audioData.length);
            }
            dis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void stopPlay() {
        audioTrack.stop();
        isPlaying = false;
        notifyStatus();
    }


    private void startRecord() {
        audioRecord.startRecording();
        notifyStatus();
        DataOutputStream dos;
        try {// 字节序：大端
            dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(audioFile)));

            isRecording = true;
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(44100 * 2 * 2);
            while (isRecording) {
                int bytesRead = audioRecord.read(byteBuffer, 44100 * 2);
                //byteBuffer.flip();
                for (int i = 0; i < bytesRead; i++) {
                    dos.write(byteBuffer.get(i));
                }
                byteBuffer.clear();
            }
            dos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void stopRecord() {
        audioRecord.stop();
        isRecording = false;
        notifyStatus();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        audioRecord.release();
        audioTrack.release();
        threadPool.shutdownNow();
    }
    private void notifyStatus(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                status.setText("isRecoding = " + isRecording + " ,isPlaying = " + isPlaying);
            }
        });
    }
}
