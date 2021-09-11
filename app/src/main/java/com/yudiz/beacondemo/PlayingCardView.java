package com.yudiz.beacondemo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * TODO: document your custom view class.
 */
public class PlayingCardView extends View{


    private Paint mPaint;
    private TextPaint mTextPaint;

    private void init() {
        mPaint=new Paint();
        mPaint.setAntiAlias(true);
        mTextPaint=new TextPaint();
        mTextPaint.setAntiAlias(true);//避免鋸齒狀
    }

    //定義一些CONSTANT來幫助我畫BOUNDARY


    static final float PIP_HOFFSET_PERCENTAGE = 0.165f;
    static final float PIP_VOFFSET1_PERCENTAGE = 0.100f;
    static final float PIP_VOFFSET2_PERCENTAGE = 0.175f;
    static final float PIP_VOFFSET3_PERCENTAGE = 0.300f;
    static final float PIP_FONT_SCALE_FACTOR = 0.01f;


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Path path=new Path();
        float cornerRadius=5;
        RectF rect= new RectF(0,0,getWidth(),getHeight());
        path.addRoundRect(rect,cornerRadius,cornerRadius,Path.Direction.CW);
        canvas.clipPath(path);

        //fill background
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.WHITE);
        canvas.drawPath(path,mPaint);

        //draw border(邊邊的黑線條)
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(3.0f);
        mPaint.setColor(Color.BLACK);
        canvas.drawPath(path,mPaint);

        //draw face card
        rect.inset(rect.width()*(1-5),rect.height()*(1-5));

        Bitmap faceImage=BitmapFactory.decodeResource(getResources(),R.drawable.map);
        canvas.drawBitmap(faceImage,null,rect,mPaint);

    }



    public PlayingCardView(Context context) {
        super(context);
        init();
    }



}
