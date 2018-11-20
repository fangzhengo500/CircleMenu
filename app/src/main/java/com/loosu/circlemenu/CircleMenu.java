package com.loosu.circlemenu;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class CircleMenu extends ViewGroup {

    private float mRadiusMax = 500;
    private float mRadiusMin = 200;

    private float mRadius = mRadiusMin;

    private boolean isOpen = false;

    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public CircleMenu(Context context) {
        this(context, null);
    }

    public CircleMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);
        this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float left = getWidth() - mRadius;
        float top = getHeight() - mRadius;
        float right = getWidth() + mRadius;
        float bottom = getHeight() + mRadius;
        RectF panelRect = new RectF(left, top, right, bottom);

        mPaint.setShadowLayer(10f, -10, -10, Color.RED);
        canvas.drawArc(panelRect, 0, 360, true, mPaint);
    }


    public void open(boolean animated) {
        isOpen = true;
        ObjectAnimator animator = ObjectAnimator.ofFloat(this, "radius", mRadiusMin, mRadiusMax);
        animator.start();
    }

    public void close(boolean animated) {
        isOpen = false;
        ObjectAnimator animator = ObjectAnimator.ofFloat(this, "radius", mRadiusMax, mRadiusMin);
        animator.start();
    }

    public float getRadius() {
        return mRadius;
    }

    public void setRadius(float radius) {
        mRadius = radius;
        postInvalidate();
    }

    public boolean isOpen() {
        return isOpen;
    }
}
