package com.wildcreek.playground.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.wildcreek.playground.R;

/**
 * Created by Administrator on 2017/12/4 0004.
 */

public class SkiImageView extends View{


    public SkiImageView(Context context) {
        super(context);
    }

    public SkiImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SkiImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SkiImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        BitmapDrawable bitmapDrawable = (BitmapDrawable) getResources().getDrawable(R.drawable.ski_1);
        Bitmap bitmap = bitmapDrawable.getBitmap();
        //500X375
        Rect destRect = new Rect(0 , 53, 540 ,458 );
        canvas.drawBitmap(bitmap,null,destRect,null);
    }
}
