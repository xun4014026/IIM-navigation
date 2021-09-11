package com.yudiz.beacondemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.util.Iterator;
import java.util.LinkedList;

import static com.yudiz.beacondemo.NavigationView.navigation_destinationNode;
import static com.yudiz.beacondemo.NavigationView.floor_now;
import static com.yudiz.beacondemo.MainActivity.sourceNode;


/**
 * TODO: document your custom view class.
 */
public class DrawLine extends View {

    private static final String TAG = "MAIN";
    private Paint mPaint;
    private Paint mCirclePaint;
    private String imageName;
    private float widthOffset ;
    private float heightOffset ;
    private float width;
    private float height;


    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);


    }




    //更新:根據使用者輸入的終點起點更新
    protected void update(){
        //mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
        //mPaint.setXfermode(null);
        Log.d(TAG, "~~~~update: ");
        invalidate();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Path path = new Path();//有這行才能隨時更新

        //設定圖的放的位置和大小
        RectF rect = new RectF(0, 0, getWidth(), getHeight());
        //canvas.clipPath(path);

        //Bitmap faceImage = BitmapFactory.decodeResource(getResources(), getFaceImageId());
        //canvas.drawBitmap(faceImage, null, rect, mPaint);

        width = getWidth();
        height = getHeight();

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

        //根據路線畫線
        LinkedList<Node> list=new LinkedList<Node>(navigation_destinationNode.getShortestPath());
        Log.d(TAG, "onDraw: ~~~"+navigation_destinationNode.getX()+","+navigation_destinationNode.getY());

        Iterator<Node> iterator=list.iterator();
        if(iterator.hasNext()){
            Log.d(TAG, "onDraw: "+"~~~");
            Node n=iterator.next();
            while(n.getFloor()!=floor_now){
                if(iterator.hasNext()){
                    n=iterator.next();
                }else{
                    break;
                }
            }
            path.moveTo((float) (n.getX()*widthOffset),(float) (n.getY()*heightOffset));

            Node k=new Node();
            while(iterator.hasNext()){
                k=iterator.next();
                if(k.getFloor()==floor_now){
                    path.lineTo((float)( k.getX()*widthOffset), (float)( k.getY()*heightOffset));
                }else{
                    continue;
                }

            }

            if(navigation_destinationNode.getFloor()==floor_now && (navigation_destinationNode.getFloor()==n.getFloor() || navigation_destinationNode.getFloor()==k.getFloor())){
                path.lineTo((float)( navigation_destinationNode.getX()*widthOffset), (float) (navigation_destinationNode.getY()*heightOffset));
            }

        }else{

            Log.d(TAG, "fail");

        }

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.rgb(255,127,106));
        mPaint.setStrokeWidth(8.0f);

        mCirclePaint.setStyle(Paint.Style.FILL);
        mCirclePaint.setColor(Color.BLUE);

        canvas.drawPath(path, mPaint);

    }

    public DrawLine(Context context) {
        super(context);
        init();
        /*if (android.os.Build.VERSION.SDK_INT >= 11)
        {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }*/
    }

    public DrawLine(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        /*if (android.os.Build.VERSION.SDK_INT >= 11)
        {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }*/
    }

    public DrawLine(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
        /*if (android.os.Build.VERSION.SDK_INT >= 11)
        {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }*/
    }

}
