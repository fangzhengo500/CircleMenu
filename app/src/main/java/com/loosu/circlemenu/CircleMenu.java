package com.loosu.circlemenu;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class CircleMenu extends ViewGroup {
    private static final String TAG = "CircleMenu";

    private float mRadiusMax = 500;
    private float mRadiusMin = 0;

    private float mRadius = mRadiusMin;

    private boolean isOpen = false;

    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private View mActionView = null;

    private List<View> mItems = new ArrayList<>();

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


        ImageView item1 = new ImageView(context);
        ImageView item2 = new ImageView(context);
        ImageView item3 = new ImageView(context);

        item1.setImageResource(R.mipmap.ic_launcher);
        item2.setImageResource(R.mipmap.ic_launcher);
        item3.setImageResource(R.mipmap.ic_launcher);

        mItems.add(item1);
        mItems.add(item2);
        mItems.add(item3);

        for (View item : mItems) {
            addView(item);
        }

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
        mActionView.layout(centerX - 50, centerY - 50, centerX + 50, centerY + 50);

        if (mRadius > 0) {
            RectF rectF = new RectF(
                    centerX - mRadius + 50,
                    centerY - mRadius + 50,
                    centerX + mRadius - 50,
                    centerY + mRadius - 50);
            Path path = new Path();
            path.addArc(rectF, 0, 360);
            PathMeasure measure = new PathMeasure(path, false);

            float distance = measure.getLength() / Math.max(mItems.size(), 1);
            float[] pos = new float[2];
            for (int i = 0; i < mItems.size(); i++) {
                measure.getPosTan(i * distance, pos, null);

                int left = (int) (pos[0] - 40);
                int top = (int) (pos[1] - 40);
                int right = (int) (pos[0] + 40);
                int bottom = (int) (pos[1] + 40);
                mItems.get(i).layout(left, top, right, bottom);

                Log.i(TAG, "onLayout: left = " + left + ", top = " + top + ", right = " + right + ", bottom = " + bottom);
            }
        } else {
            for (int i = 0; i < mItems.size(); i++) {
                int left = centerX - 40;
                int top = centerY - 40;
                int right = centerX + 40;
                int bottom = centerY + 40;
                mItems.get(i).layout(left, top, right, bottom);

                Log.i(TAG, "onLayout: left = " + left + ", top = " + top + ", right = " + right + ", bottom = " + bottom);
            }
        }
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
        requestLayout();
    }

    public boolean isOpen() {
        return isOpen;
    }
}
