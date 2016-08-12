package com.circleprogressview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import com.circleprogressview.R;

/**
 * Created by CJX on 2016-8-12.
 *
 * 自定义属性集
 * 注意资源文件的使用
 */
public class CircleProgressBar extends View{

    private Context context = null;

    //属性集
    private TypedArray typedArray = null;
    //圆环颜色
    private int circleColor;
    //圆环进度颜色
    private int circleProgressColor;
    //圆环宽度
    private float circleWidth;
    //字体颜色
    private int textColor;
    //字体大小
    private float textSize;
    //字体是否显示
    private boolean textVisible;
    //进度最大值
    private int progressMax;
    //显示风格
    private int style;
    //扇形
    public static final int FAN = 0;
    //弧形
    public static final int SWEEP = 1;

    private Paint paint = null;

    private int progress;

    public CircleProgressBar(Context context) {
        this(context,null);
    }

    public CircleProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CircleProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;

        paint = new Paint();
        getAttrs(attrs);
    }

    /**
     * 获取自定义的属性集
     * */
    private void getAttrs(AttributeSet attrs){
        typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleProgressBar);

        circleColor = typedArray.getColor(R.styleable.CircleProgressBar_circleColor,Color.GREEN);
        circleProgressColor = typedArray.getColor(R.styleable.CircleProgressBar_circleProgressColor,Color.RED);
        circleWidth = typedArray.getDimension(R.styleable.CircleProgressBar_circleWidth,5);
        textColor = typedArray.getColor(R.styleable.CircleProgressBar_textColor,Color.BLACK);
        textSize = typedArray.getDimension(R.styleable.CircleProgressBar_textSize,15);
        textVisible = typedArray.getBoolean(R.styleable.CircleProgressBar_textVisible,true);
        progressMax = typedArray.getInteger(R.styleable.CircleProgressBar_progressMax,100);
        style = typedArray.getInt(R.styleable.CircleProgressBar_style,1);

        //释放资源
        typedArray.recycle();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //----------画最外层的圆环--------------
        //圆环中心X坐标
        int centerX = getWidth() / 2;
        //半径
        int radius = (int)(centerX - circleWidth / 2);
        //颜色
        paint.setColor(circleColor);
        //宽度
        paint.setStrokeWidth(circleWidth);
        //空心
        paint.setStyle(Paint.Style.STROKE);
        //消除锯齿
        paint.setAntiAlias(true);
        //画出圆环
        canvas.drawCircle(centerX, centerX, radius, paint);

        //----------画进度百分比(环内显示) ------------------
        paint.setStrokeWidth(0);
        paint.setColor(textColor);
        paint.setTextSize(textSize);
        //设置字体
        paint.setTypeface(Typeface.DEFAULT_BOLD);

        int percent = (int)(((float)progress / (float)progressMax) * 100);
        //根据字体的宽度设置在圆环中间
        float textWidth = paint.measureText(percent + "%");
        //画出进度百分比
        if(textVisible && percent != 0 && style == SWEEP){
            canvas.drawText(percent + "%", centerX - textWidth / 2, centerX + textSize / 2, paint);
        }

        //----------画圆弧(扇形)进度-----------
        //设置圆环的宽度
        paint.setStrokeWidth(circleWidth);
        //设置进度的颜色
        paint.setColor(circleProgressColor);
        //用于定义的圆弧的形状和大小的界限
        RectF oval = new RectF(centerX - radius, centerX - radius, centerX + radius, centerX + radius);

        switch (style){
            case FAN:       //扇形
                paint.setStyle(Paint.Style.FILL_AND_STROKE);
                canvas.drawArc(oval, -90, 360 * progress / progressMax, true, paint);

                break;
            case SWEEP:     //弧形
                paint.setStyle(Paint.Style.STROKE);
                canvas.drawArc(oval, -90, 360 * progress / progressMax, false, paint);

                break;
        }

    }

    /**
     * 多线程刷新考虑
     * */
    public synchronized int getCircleColor() {
        return circleColor;
    }

    public synchronized void setCircleColor(int circleColor) {
        this.circleColor = circleColor;
    }

    public synchronized int getCircleProgressColor() {
        return circleProgressColor;
    }

    public synchronized void setCircleProgressColor(int circleProgressColor) {
        this.circleProgressColor = circleProgressColor;
    }

    public synchronized float getCircleWidth() {
        return circleWidth;
    }

    public synchronized void setCircleWidth(float circleWidth) {
        this.circleWidth = circleWidth;
    }

    public synchronized int getTextColor() {
        return textColor;
    }

    public synchronized void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public synchronized float getTextSize() {
        return textSize;
    }

    public synchronized void setTextSize(float textSize) {
        this.textSize = textSize;
    }

    public synchronized boolean isTextVisible() {
        return textVisible;
    }

    public synchronized void setTextVisible(boolean textVisible) {
        this.textVisible = textVisible;
    }

    public synchronized int getProgressMax() {
        return progressMax;
    }

    public synchronized void setProgressMax(int progressMax) {
        if(progressMax < 0){
            throw new IllegalArgumentException("max not less than 0");
        }
        this.progressMax = progressMax;
    }

    public synchronized int getStyle() {
        return style;
    }

    public synchronized void setStyle(int style) {
        this.style = style;
    }

    public synchronized int getProgress() {
        return progress;
    }

    public synchronized void setProgress(int progress) {
        if(progress < 0){
            throw new IllegalArgumentException("progress not less than 0");
        }

        if (progress > progressMax){
            progress = progressMax;
        }

        this.progress = progress;
        //能在非UI线程刷新
        postInvalidate();
    }
}
