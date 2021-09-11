package com.yudiz.beacondemo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;

import static com.yudiz.beacondemo.MainActivity.sourceNode;

/**
 * TODO: document your custom view class.
 */
public class MyView extends View implements ScaleGestureDetector.OnScaleGestureListener{

    private Paint mPaint;
    private Paint mCirclePaint;
    private String imageName ="iim_1f_1125_50_90";

    public static float  width;
    public static float  height;
    public static float  widthOffset;
    public static float  heightOffset;
    private ImageView markerview;

    private ScaleGestureDetector scaleGestureDetector;


    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        markerview=findViewById(R.id.mapmaker);
    }

    protected void setImageName(String name){
        imageName = name;
        invalidate();
    }

    protected String getImageName(){
        return imageName;
    }

    private int getFaceImageId() {
        return getResources().getIdentifier(imageName, "drawable", getContext().getPackageName());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //設定圖的放的位置和大小

        Bitmap faceImage = BitmapFactory.decodeResource(getResources(), getFaceImageId());
        RectF rect = new RectF(0, 0, getWidth(), getHeight());
        canvas.drawBitmap(faceImage, null, rect, mPaint);

        Path path = new Path();

        width = getWidth();
        height =getHeight();

        //設定地圖上的一格是多寬，getWidth() and getHeight()可以取得螢幕的長和寬
         widthOffset = width / 50;
         heightOffset = height / 90;



/*
        //測試畫線和座標點
        path.moveTo(0, 0); //起點
        path.lineTo(100, 0);
        path.lineTo(100, 200);
        path.lineTo(500, 200);
        path.lineTo(500, 500);
        path.lineTo(700, 500);
        path.lineTo(700, 900);


        canvas.drawCircle(0, 0, 20, mCirclePaint);
        canvas.drawCircle(100, 0, 20, mCirclePaint);
        canvas.drawCircle(100, 200, 20, mCirclePaint);
        canvas.drawCircle(500, 200, 20, mCirclePaint);
        canvas.drawCircle(500, 500, 20, mCirclePaint);
        canvas.drawCircle(700, 500, 20, mCirclePaint);
        canvas.drawCircle(700, 900, 20, mCirclePaint);
*/

        //因為整張圖的最左上角是(-6,0），所以X軸要多補6給它
        /*path.moveTo(0, 0); //起點
        path.lineTo((30+6)*widthOffset, 0*heightOffset);
        path.lineTo((30+6)*widthOffset, 74*heightOffset);
        path.lineTo((6+6)*widthOffset, 74*heightOffset);
        path.lineTo((6+6)*widthOffset, 80*heightOffset);*/

        //DrawLine drawLine = (DrawLine) findViewById(R.id.drawLine);

/*
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(8.0f);

        mCirclePaint.setStyle(Paint.Style.FILL);
        mCirclePaint.setColor(Color.BLUE);



        canvas.drawPath(path, mPaint);*/
    }



    public MyView(Context context) {
        super(context);
        init();
    }

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        return false;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        return false;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {

    }
}


