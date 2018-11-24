package com.loosu.floatingmenu.circlemenu;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Property;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.loosu.floatingmenu.IMenu;
import com.loosu.floatingmenu.R;

import java.util.ArrayList;
import java.util.List;

public class CircleMenu extends ViewGroup implements IMenu {
    private static final String TAG = "CircleMenu";

    public static final Property<CircleMenu, Float> RADIUS = new Property<CircleMenu, Float>(Float.class, "radius") {

        @Override
        public void set(CircleMenu object, Float value) {
            object.setRadius(value);
        }

        @Override
        public Float get(CircleMenu object) {
            return object.getRadius();
        }
    };

    private float mRadiusMax = 500;
    private float mRadiusMin = 0;

    private float mStartAngle = 180;
    private float mSweepAngle = 90;

    private float mRadius = mRadiusMin;

    private IAnimatedAdapter<CircleMenu> mAnimatedAdapter = new CircleMenuAnimatorAdapter();

    private boolean isOpen = false;

    private OnStateChangeListener mStateChangeListener = null;

    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private ActionItem mItemAction = null;

    private List<IMenu.IItem> mItems = new ArrayList<>();


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
        item1.setBackgroundColor(0xffff6666);
        item2.setBackgroundColor(0xff66ff66);
        item3.setBackgroundColor(0xff6666ff);

        mItems.add(new MenuItem(item1));
        mItems.add(new MenuItem(item2));
        mItems.add(new MenuItem(item3));

        for (IItem item : mItems) {
            addView(item.getView());
        }

        ImageView actionView = new ImageView(context);
        actionView.setImageResource(R.drawable.icon_github);

        mItemAction = new ActionItem(actionView);
        addView(mItemAction.getView());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mRadiusMax = Math.min(w, h) / 4;
        mRadiusMin = mRadiusMax / 4;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int centerX = (r - l) / 2;
        int centerY = (b - t) / 2;
        mItemAction.getView().layout(centerX - 50, centerY - 50, centerX + 50, centerY + 50);

        for (IItem item : mItems) {
            item.getView().layout(centerX - 50, centerY - 50, centerX + 50, centerY + 50);
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

        mPaint.setColor(Color.WHITE);
        mPaint.setShadowLayer(10f, 0, 0, 0x33333333);
        canvas.drawArc(panelRect, 0, 360, true, mPaint);
    }

    @Override
    public void open(boolean animated) {
        isOpen = true;

        changeState(State.START_OPEN);

        Animator animator = mAnimatedAdapter.obtainOpenAnimator(this);
        if (animator != null) {
            animator.addListener(mMenuOpenListener);
            animator.start();
        }
    }

    @Override
    public void close(boolean animated) {

        isOpen = false;

        changeState(State.START_CLOSE);
        Animator animator = mAnimatedAdapter.obtainCloseAnimator(this);
        if (animator != null) {
            animator.addListener(mMenuCloseListener);
            animator.start();
        }
    }

    private void changeState(State state) {
        if (mStateChangeListener != null) {
            mStateChangeListener.onMenuStateChange(this, state);
        }
    }

    public ActionItem getItemAction() {
        return mItemAction;
    }

    public void setItemAction(ActionItem itemAction) {
        mItemAction = itemAction;
    }

    public List<IItem> getItems() {
        return mItems;
    }

    public void setItems(List<IItem> items) {
        mItems = items;
    }

    public float getRadius() {
        return mRadius;
    }

    public void setRadius(float radius) {
        mRadius = radius;
        postInvalidate();
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

    public OnStateChangeListener getStateChangeListener() {
        return mStateChangeListener;
    }

    public void setStateChangeListener(OnStateChangeListener stateChangeListener) {
        mStateChangeListener = stateChangeListener;
    }

    public boolean isOpen() {
        return isOpen;
    }

    private final AnimatorListenerAdapter mMenuOpenListener = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationStart(Animator animation) {
            changeState(State.SETTING);
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            changeState(State.OPENED);

        }
    };

    private final AnimatorListenerAdapter mMenuCloseListener = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationStart(Animator animation) {
            changeState(State.SETTING);
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            changeState(State.CLOSED);
        }
    };
}
