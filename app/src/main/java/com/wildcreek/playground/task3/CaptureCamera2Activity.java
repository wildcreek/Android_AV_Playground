package com.wildcreek.playground.task3;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;

import com.wildcreek.playground.R;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CaptureCamera2Activity extends AppCompatActivity implements TextureView.SurfaceTextureListener, Camera.PreviewCallback {

    private ExecutorService threadPool  = Executors.newSingleThreadExecutor();
    private TextureView mTextureView;
    private Camera mCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture_camera2);

        mTextureView = findViewById(R.id.act_capture_camera_texture);
        mTextureView.setSurfaceTextureListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        releaseCameraAndPreview();
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        try {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                //ask for authorisation
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 50);
            }else {
                releaseCameraAndPreview();
                mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
                final Camera.Parameters parameters = mCamera.getParameters();
                parameters.setPictureFormat(ImageFormat.NV21);
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
                mCamera.setParameters(parameters);
                //设置预览的顺时针旋转角度，该方法影响预览的图像和拍照后图片的展示方向，但不影响 PreviewCallback的 onPreviewFrame的二进制数据序列
                setCameraDisplayOrientation(CaptureCamera2Activity.this , 1,mCamera);

                if(mCamera != null){
                    //预览必须要用到surface 或者 surfaceTexture ;拍照必须先设置预览
                    mCamera.setPreviewTexture(surface);
                    mCamera.setPreviewCallback(CaptureCamera2Activity.this);
                    mCamera.startPreview();
                }
            }

        } catch (IOException ioe) {
            // Something bad happened
            Log.e(getString(R.string.app_name), "failed to open Camera");
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        // Ignored, Camera does all the work for us
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        releaseCameraAndPreview();
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        // Invoked every time there's a new Camera preview frame
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {

    }

    private void releaseCameraAndPreview() {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    public static void setCameraDisplayOrientation(Activity activity, int cameraId, android.hardware.Camera camera) {
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


}
