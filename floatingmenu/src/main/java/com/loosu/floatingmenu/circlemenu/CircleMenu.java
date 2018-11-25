package com.loosu.floatingmenu.circlemenu;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Property;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.loosu.floatingmenu.IMenu;

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

    public enum Anchor {
        CENTER,
        CENTER_LEFT,
        CENTER_TOP,
        CENTER_RIGHT,
        CENTER_BOTTOM,
        LEFT_TOP,
        LEFT_BOTTOM,
        RIGHT_TOP,
        RIGHT_BOTTOM,
    }

    // menu
    private State mState = State.CLOSED;
    private BaseItem mActionItem = null;
    private List<IMenu.IItem> mItems = new ArrayList<>();

    // anchor
    private Anchor mAnchor = Anchor.CENTER;
    private int mAnchorOffsetX = 0;
    private int mAnchorOffsetY = 0;

    // radius
    private float mRadiusMax = 500;
    private float mRadiusMin = 0;
    private float mRadius = mRadiusMin;

    // angle
    private float mStartAngle = 180;
    private float mSweepAngle = 90;

    // animated
    private Animator mAnimator;
    private IAnimatedAdapter<CircleMenu> mAnimatedAdapter = new CircleMenuAnimatorAdapter();        // 动画适配器

    private OnStateChangeListener mStateChangeListener = null;

    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);


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

        mItems.add(new BaseItem(item1, 60, 60));
        mItems.add(new BaseItem(item2, 60, 60));
        mItems.add(new BaseItem(item3, 60, 60));

        for (IItem item : mItems) {
            addView(item.getView());
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        Log.d(TAG, "onSizeChanged: w = " + w + ", h = " + h + ", oldw = " + oldw + ", oldh = " + oldh);
        mRadiusMax = Math.min(w, h) / 4;
        mRadiusMin = mRadiusMax / 4;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.d(TAG, "onLayout: changed = " + changed + ", l = " + l + ", t = " + t + ", r = " + r + ", b = " + b);

        int anchorX = mAnchorOffsetX;
        int anchorY = mAnchorOffsetY;

        switch (mAnchor) {
            case CENTER:
                anchorX += (r - l) / 2;
                anchorY += (b - t) / 2;
                break;
            case CENTER_LEFT:
                anchorX += l;
                anchorY += (b - t) / 2;
                break;
            case CENTER_TOP:
                anchorX += (r - l) / 2;
                anchorY += t;
                break;
            case CENTER_RIGHT:
                anchorX += r;
                anchorY += (b - t) / 2;
                break;
            case CENTER_BOTTOM:
                anchorX += (r - l) / 2;
                anchorY += b;
                break;
            case LEFT_TOP:
                anchorX += l;
                anchorY += t;
                break;
            case LEFT_BOTTOM:
                anchorX += l;
                anchorY += b;
                break;
            case RIGHT_TOP:
                anchorX += r;
                anchorY += t;
                break;
            case RIGHT_BOTTOM:
                anchorX += r;
                anchorY += b;
                break;
        }

        if (mActionItem != null) {
            View actionView = mActionItem.getView();
            LayoutParams params = actionView.getLayoutParams();
            int width = mActionItem.getWidht();
            int height = mActionItem.getHeight();
            actionView.layout(anchorX - width / 2, anchorY - height / 2, anchorX + width / 2, anchorY + height / 2);
        }

        for (IItem item : mItems) {
            item.getView().layout(anchorX - 50, anchorY - 50, anchorX + 50, anchorY + 50);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mActionItem != null) {
            View actionView = mActionItem.getView();
            int anchorX = (actionView.getRight() + actionView.getLeft()) / 2;
            int anchorY = (actionView.getBottom() + actionView.getTop()) / 2;

            mPaint.setColor(Color.WHITE);
            mPaint.setShadowLayer(10f, 0, 0, 0x33333333);

            canvas.drawCircle(anchorX, anchorY, mRadius, mPaint);
        }

    }

    @Override
    public void open(boolean animated) {
        cancelAnimated();
        changeState(State.START_OPEN);

        mAnimator = mAnimatedAdapter.obtainOpenAnimator(this);
        if (mAnimator != null) {
            mAnimator.addListener(mMenuOpenListener);
            mAnimator.start();
        }
    }


    @Override
    public void close(boolean animated) {
        cancelAnimated();
        changeState(State.START_CLOSE);
        mAnimator = mAnimatedAdapter.obtainCloseAnimator(this);
        if (mAnimator != null) {
            mAnimator.addListener(mMenuCloseListener);
            mAnimator.start();
        }
    }

    private void cancelAnimated() {
        if (mAnimator != null) {
            mAnimator.cancel();
        }
    }

    private void changeState(State state) {
        mState = state;
        if (mStateChangeListener != null) {
            mStateChangeListener.onMenuStateChange(this, state);
        }
    }

    @NonNull
    public Anchor getAnchor() {
        return mAnchor;
    }

    public void setAnchor(Anchor anchor) {
        if (anchor == null) {
            return;
        }
        if (mAnimator != null) {
            mAnimator.end();
        }
        mAnchor = anchor;
        requestLayout();
    }

    public int getAnchorOffsetX() {
        return mAnchorOffsetX;
    }

    public void setAnchorOffsetX(int anchorOffsetX) {
        mAnchorOffsetX = anchorOffsetX;
        requestLayout();
    }

    public int getAnchorOffsetY() {
        return mAnchorOffsetY;
    }

    public void setAnchorOffsetY(int anchorOffsetY) {
        mAnchorOffsetY = anchorOffsetY;
        requestLayout();
    }

    public BaseItem getActionItem() {
        return mActionItem;
    }

    public void setActionItem(BaseItem actionItem) {
        mActionItem = actionItem;
        if (actionItem != null) {
            addView(actionItem.getView());
        }
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

    public State getState() {
        return mState;
    }

    public OnStateChangeListener getStateChangeListener() {
        return mStateChangeListener;
    }

    public void setStateChangeListener(OnStateChangeListener stateChangeListener) {
        mStateChangeListener = stateChangeListener;
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
