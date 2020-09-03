package com.example.myweather;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

public class Thermometer extends View {
    private static final String TAG = "ThermometerView";

    private int thermometerColor = Color.BLUE;
    private int levelColor = Color.RED;
    private int levelPressedColor = Color.RED;
    private RectF thermometerRectangle = new RectF();
    private Rect levelRectangle = new Rect();
    private int cx;
    private int cy;
    private int r1;
    private int r2;
    private Paint levelPressedPaint;
    private Paint thermometerPaint;
    private Paint levelPaint;
    private int width = 0;
    private int height = 0;

    private int level = 100;
    private final static int padding = 10;
    private final static int round = 5;

    public Thermometer(Context context) {
        super(context);
        init();
    }

    public Thermometer(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttr(context, attrs);
        init();
    }

    public Thermometer(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(context, attrs);
        init();
    }

    public void setLevel(int level) {
        this.level = level;
        invalidate();
    }

    private void initAttr(Context context, AttributeSet attrs) {

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ThermometerView, 0,0);
        thermometerColor = typedArray.getColor(R.styleable.ThermometerView_thermometer_color, thermometerColor);
        levelColor = typedArray.getColor(R.styleable.ThermometerView_level_color, levelColor);
        levelPressedColor = typedArray.getColor(R.styleable.ThermometerView_level_pressed_color, Color.RED);
        level = typedArray.getInteger(R.styleable.ThermometerView_level, 0);
       typedArray.recycle();
    }

    private void init(){
        thermometerPaint = new Paint();
        thermometerPaint.setColor(thermometerColor);
        thermometerPaint.setStyle(Paint.Style.FILL);
        levelPaint = new Paint();
        levelPaint.setColor(levelColor);
        levelPaint.setStyle(Paint.Style.FILL);
        // Задать "краску" для нажатия на элемент +
        levelPressedPaint = new Paint();
        levelPressedPaint.setColor(levelPressedColor);
        levelPressedPaint.setStyle(Paint.Style.FILL);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        width = w - getPaddingLeft() - getPaddingRight();
        height = h - getPaddingTop() - getPaddingBottom();
        // Отрисовка батареи
        cx = (width)/2;
        cy = height-width/2;
        r1 = width /2;
        r2 = width /2-padding;

        thermometerRectangle.set(cx/2, 0,
                cx+(cx/2),
                height - padding);
        setlevelRectangle();

    }

    private void setlevelRectangle(){
        levelRectangle.set(
                cx/2+padding, (int)((height-width) *(1-(double)level/(double)100)) + (2 * padding),
                cx+cx/2-padding,
                height-2*padding);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d(TAG, "onDraw");

        setlevelRectangle();
        canvas.drawRoundRect(thermometerRectangle, round, round, thermometerPaint);
        canvas.drawCircle(cx,cy,r1, thermometerPaint);
        canvas.drawCircle(cx,cy,r2, levelPaint);
        canvas.drawRect(levelRectangle, levelPaint);
    }

}
