package com.example.weather_app_drawer_second_java.ui.home;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.weather_app_drawer_second_java.R;
import com.google.gson.TypeAdapter;

public class ThermometerView extends View {

    private int thermometerColour = Color.BLACK;
    private int levelColour = Color.RED;
    private RectF thermometerRectangle = new RectF();
    private Rect hermometerLevelRectangle = new Rect();
    private Paint thermometerPaint;
    private Paint levelPaint;
    private int width = 0;
    private int height = 0;

    private int maxLevel = 50;
    private final static int padding = 5;
    private final static int round =10;
    private final static int headWidth = 10;

    public ThermometerView(Context context) {
        super(context);
        init();
    }

    public ThermometerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttr(context, attrs);
        init();

    }

    public ThermometerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(context, attrs);
        init();

    }

    public ThermometerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initAttr(context, attrs);
        init();
    }
    private void initAttr(Context context, AttributeSet attributeSet){
        TypedArray typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.ThermometerView,0,0);
        thermometerColour = typedArray.getColor(R.styleable.ThermometerView_thermometer_color,Color.BLACK);
        levelColour = typedArray.getColor(R.styleable.ThermometerView_level_color,Color.RED);
        levelColour = typedArray.getInteger(R.styleable.ThermometerView_level,20);
        typedArray.recycle();
    }
    private void init(){
        thermometerPaint = new Paint();
        thermometerPaint.setColor(thermometerColour);
        thermometerPaint.setStyle(Paint.Style.FILL);
        levelPaint = new Paint();
        levelPaint.setColor(levelColour);
        levelPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRoundRect(thermometerRectangle, round, round, thermometerPaint);
            canvas.drawRect(hermometerLevelRectangle, levelPaint);
        }


}
