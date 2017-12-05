package com.wildcreek.playground.image;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.wildcreek.playground.R;

public class DrawImageActivity extends AppCompatActivity {

    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_image);
        surfaceView = findViewById(R.id.act_draw_sv);
        surfaceHolder = surfaceView.getHolder();
        //位于window上面，防止被覆盖
        surfaceView.setZOrderOnTop(true);
        //将默认OPAQUE修改为TRANSLUCENT
        surfaceHolder.setFormat(PixelFormat.TRANSLUCENT);
        surfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        drawImage(surfaceHolder);
                    }
                }).start();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });
    }
    private void drawImage(SurfaceHolder surfaceHolder){
        Canvas canvas = surfaceHolder.lockCanvas();
        BitmapDrawable bitmapDrawable = (BitmapDrawable) getResources().getDrawable(R.drawable.ski_1);
        Bitmap bitmap = bitmapDrawable.getBitmap();
        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();
        float ratio  = bitmapWidth / (float)bitmapHeight;
        int surfaceWidth = surfaceView.getWidth();
        int surfaceHeight = surfaceView.getHeight();
        Log.i("DrawImageActivity" , "surface  = " + surfaceWidth +" X " + surfaceHeight);
        Log.i("DrawImageActivity" , "bitmap  = " + bitmapWidth +" X " + bitmapHeight + ",ratio = " + ratio );
        Rect srcRect = new Rect(0 , 0,bitmapWidth,bitmapHeight);
        Rect destRect ;
        if((float)bitmapWidth / bitmapHeight > (float) surfaceWidth / surfaceHeight){
            //SurfaceView 上下留白
            Log.i("DrawImageActivity" , "SurfaceView 上下留白");
            destRect = new Rect( 0 ,(int)(surfaceHeight - surfaceHeight / ratio) / 2 ,surfaceWidth ,(int)(surfaceHeight + surfaceHeight / ratio) / 2 );
        }else{
            // SurfaceView 左右留白
            destRect = new Rect( (int)(surfaceWidth - surfaceWidth * ratio) / 2 ,0 ,(int)(surfaceWidth +  surfaceWidth * ratio) / 2 , surfaceHeight);
         }
         Log.i("DrawImageActivity" , destRect.toString());
        canvas.drawBitmap(bitmap,srcRect,destRect,null);
        surfaceHolder.unlockCanvasAndPost(canvas);
    }

}
