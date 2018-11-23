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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CircleMenu extends ViewGroup {
    private static final String TAG = "CircleMenu";

    private float mRadiusMax = 500;
    private float mRadiusMin = 0;

    private float mStartAngle = 180;
    private float mSweepAngle = 90;

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


        TextView item1 = new TextView(context);
        TextView item2 = new TextView(context);
        TextView item3 = new TextView(context);
        item1.setText("1");
        item2.setText("2");
        item3.setText("3");
        item1.setBackgroundResource(R.mipmap.ic_launcher);
        item2.setBackgroundResource(R.mipmap.ic_launcher);
        item3.setBackgroundResource(R.mipmap.ic_launcher);

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
        //Log.i(TAG, "onLayout: changed = " + changed + ", left = " + l + ", top = " + t + ", right = " + r + ", bottom = " + b);
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
            path.addArc(rectF, mStartAngle, mSweepAngle);
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

                //Log.i(TAG, "onLayout: left = " + left + ", top = " + top + ", right = " + right + ", bottom = " + bottom);
            }
        } else {
            for (int i = 0; i < mItems.size(); i++) {
                int left = centerX - 40;
                int top = centerY - 40;
                int right = centerX + 40;
                int bottom = centerY + 40;
                mItems.get(i).layout(left, top, right, bottom);

                //Log.i(TAG, "onLayout: left = " + left + ", top = " + top + ", right = " + right + ", bottom = " + bottom);
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
        Log.i(TAG, "open: " + mRadiusMin + " -> " + mRadiusMax);
        isOpen = true;
        ObjectAnimator animator = ObjectAnimator.ofFloat(this, "radius", mRadiusMin, mRadiusMax);
        animator.start();
    }

    public void close(boolean animated) {
        Log.i(TAG, "open: " + mRadiusMax + " -> " + mRadiusMin);
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

    public float getRadiusMax() {
        return mRadiusMax;
    }

    public void setRadiusMax(float radiusMax) {
        mRadiusMax = radiusMax;
    }

    public float getRadiusMin() {
        return mRadiusMin;
    }

    public void setRadiusMin(float radiusMin) {
        mRadiusMin = radiusMin;
    }

    public float getStartAngle() {
        return mStartAngle;
    }

    public void setStartAngle(float startAngle) {
        mStartAngle = startAngle;
        requestLayout();
    }

    public float getSweepAngle() {
        return mSweepAngle;
    }

    public void setSweepAngle(float sweepAngle) {
        mSweepAngle = sweepAngle;
        requestLayout();
    }

    public boolean isOpen() {
        return isOpen;
    }
}
