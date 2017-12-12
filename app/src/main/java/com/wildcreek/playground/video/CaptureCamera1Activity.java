package com.wildcreek.playground.video;

import android.app.Activity;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.wildcreek.playground.R;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CaptureCamera1Activity extends AppCompatActivity implements Camera.PreviewCallback {

    private ExecutorService threadPool  = Executors.newSingleThreadExecutor();
    private SurfaceView surfaceView ;
    private SurfaceHolder surfaceHolder;
    private Camera mCamera;
    private OrientationEventListener mOrientationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture_camera1);

        surfaceView = findViewById(R.id.sv_act_capture_camera);
        surfaceHolder = surfaceView.getHolder();
    }

    @Override
    protected void onResume() {
        super.onResume();
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
                final Camera.Parameters parameters = mCamera.getParameters();
                //parameters.setPreviewSize(640 ,480);
                parameters.setPictureFormat(ImageFormat.NV21);
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
                //设置摄像头的顺时针旋转角度
                setCameraRotation(parameters);
                mCamera.setParameters(parameters);
                //设置预览的顺时针旋转角度，该方法影响预览的图像和拍照后图片的展示方向，但不影响 PreviewCallback的 onPreviewFrame的二进制数据序列
                setCameraDisplayOrientation(CaptureCamera1Activity.this , 1,mCamera);
                try {
                    mCamera.setPreviewDisplay(surfaceHolder);
                    mCamera.setPreviewCallback(CaptureCamera1Activity.this);
                    mCamera.startPreview();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        //FileUtils.saveMediaData(data ,FileUtils.MEDIA_TYPE_IMAGE);
    }
    @Override
    protected void onPause() {
        super.onPause();
        if(mCamera != null){
            mCamera.stopPreview();
            mCamera.release();
        }
    }
    //设置相对于摄像头视角的顺时针旋转角度，该方法影响Camera.pictureCallback的图像方向
    //OrientationEventListener监听正常手机角度变化，CameraInfo.orientation 是与正常状态手机角度差值，
    //为达到预览实景的效果，后置摄像头需要设置成两者之和，前置摄像头需要设置成两者之差
    //假设手机顺时针旋转270度，假设后置摄像头的上方为手机右侧，即camera的orientation为90度，所以应该设成两者之和360度
    private void setCameraRotation(final Camera.Parameters parameters){
        mOrientationListener = new OrientationEventListener(CaptureCamera1Activity.this , SensorManager.SENSOR_DELAY_NORMAL) {
            @Override
            public void onOrientationChanged(int orientation) {
                Log.e("" ,"onOrientationChanged = " + orientation);
                if (orientation == ORIENTATION_UNKNOWN) return;
                Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
                android.hardware.Camera.getCameraInfo(1, info);
                orientation = (orientation + 45) / 90 * 90;
                int rotation = 0;
                if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                    rotation = (info.orientation - orientation + 360) % 360;
                } else {  // back-facing camera
                    rotation = (info.orientation + orientation) % 360;
                }
                parameters.setRotation(rotation);
            }
        };
        if (mOrientationListener.canDetectOrientation()) {
            Log.e("" ,"onOrientationChanged can DetectOrientation" );
            mOrientationListener.enable();
        } else {
            Log.e("" ,"onOrientationChanged can not DetectOrientation" );
            mOrientationListener.disable();
        }
    }
    public static void setCameraDisplayOrientation(Activity activity,int cameraId, android.hardware.Camera camera) {
        android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraId, info);
        //屏幕内容的旋转方向：手机逆时针旋转90，作为补偿，该方法返回Surface.ROTATION_90
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0: degrees = 0; break;
            case Surface.ROTATION_90: degrees = 90; break;
            case Surface.ROTATION_180: degrees = 180; break;
            case Surface.ROTATION_270: degrees = 270; break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mOrientationListener.disable();
    }

}
