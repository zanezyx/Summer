package com.deter.TaxManager.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

/**
 * Created by xiaolu on 17-9-9.
 */

public class ProgressImgView extends AppCompatImageView {

    private final int STATE_TYPE_INIT = 0;

    private final int STATE_TYPE_LOADING = 1;

    private final int STATE_TYPE_SUCCESS = 2;

    private Paint paint;

    private int widthMeasureSize;

    private int heightMeasureSize;

    private float centerPointX;

    private float centerPointY;

    private float outerR;

    private float innerR;

    private int stateType = STATE_TYPE_INIT;

    private int animationLine = 0;

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            postInvalidate();
            postDelayed(this, 300);
        }
    };

    public ProgressImgView(Context context) {
        super(context);
        init();
    }

    public ProgressImgView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ProgressImgView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.GRAY);
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.STROKE);
    }

    public void setState(int stateType) {
        this.stateType = stateType;
        removeCallbacks(runnable);
        if (stateType == STATE_TYPE_LOADING && isAttachedToWindow()) {
            post(runnable);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        widthMeasureSize = getMeasuredWidth();
        heightMeasureSize = getMeasuredHeight();
        centerPointX = widthMeasureSize / 2;
        centerPointY = heightMeasureSize / 2;
        outerR = (Math.min(widthMeasureSize, heightMeasureSize) - 20) / 2;
        innerR = outerR / 2;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        if (stateType != STATE_TYPE_LOADING) {
            super.onDraw(canvas);
            return;
        }
        drawCircleProgress(canvas);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        removeCallbacks(runnable);
        if (stateType == STATE_TYPE_LOADING) {
            post(runnable);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        removeCallbacks(runnable);
        super.onDetachedFromWindow();
    }

    private void drawCircleProgress(Canvas canvas) {
        Path path = new Path();
        for (int i = 0; i < 8; i++) {
            path.reset();
            double anger = (i * 2 * Math.PI / 8);
            float pointInnx = centerPointX + (float) (innerR * Math.cos(anger));
            float pointInny = centerPointY + (float) (innerR * Math.sin(anger));
            float pointOutx = centerPointX + (float) (outerR * Math.cos(anger));
            float pointOuty = centerPointY + (float) (outerR * Math.sin(anger));
            path.moveTo(pointInnx, pointInny);
            path.lineTo(pointOutx, pointOuty);
            if (animationLine == i || animationLine == i + 1) {
                paint.setColor(Color.BLACK);
            } else {
                paint.setColor(Color.GRAY);
            }
            canvas.drawPath(path, paint);
        }
        animationLine = (animationLine + 1) % 8;
    }
}
