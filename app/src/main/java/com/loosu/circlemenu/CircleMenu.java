package com.loosu.circlemenu;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class CircleMenu extends ViewGroup {
    private static final String TAG = "CircleMenu";

    private float mRadiusMax = 500;
    private float mRadiusMin = 0;

    private float mRadius = mRadiusMin;

    private boolean isOpen = false;

    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private View mActionView = null;

    public CircleMenu(Context context) {
        this(context, null);
    }

    public CircleMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setWillNotDraw(false);
        this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        ImageView actionView = new ImageView(context);
        actionView.setImageResource(R.mipmap.ic_launcher_round);
        mActionView = actionView;
        addView(actionView);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.i(TAG, "onLayout: changed = " + changed + ", left = " + l + ", top = " + t + ", right = " + r + ", bottom = " + b);
        int centerX = (r - l) / 2;
        int centerY = (b - t) / 2;
        mActionView.layout(centerX - 50, centerY - 50,centerX + 50, centerY + 50 );
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float left = getWidth() / 2 - mRadius;
        float top = getHeight() / 2 - mRadius;
        float right = getWidth() / 2 + mRadius;
        float bottom = getHeight() / 2 + mRadius;
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
        invalidate();
    }

    public boolean isOpen() {
        return isOpen;
    }
}
