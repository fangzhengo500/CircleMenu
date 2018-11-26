package com.loosu.floatingmenu.circlemenu;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.view.View;

import com.loosu.floatingmenu.IMenu;

import java.util.ArrayList;
import java.util.List;

public class CircleMenuAnimatorAdapter implements IMenu.IAnimatedAdapter<CircleMenu> {
    private final long ANIMATION_DURATION = 200;

    @Override
    public Animator onLayout(@NonNull CircleMenu menu) {
        if (menu.getState() == IMenu.State.OPENED) {
            for (IMenu.IItem iItem : menu.getItems()) {
                View itemView = iItem.getView();
                itemView.setAlpha(1);
                itemView.setScaleX(1);
                itemView.setScaleY(1);
                itemView.setVisibility(View.VISIBLE);
            }

        } else if (menu.getState() == IMenu.State.CLOSED) {
            for (IMenu.IItem iItem : menu.getItems()) {
                View itemView = iItem.getView();
                itemView.setAlpha(0);
                itemView.setScaleX(0);
                itemView.setScaleY(0);
                itemView.setVisibility(View.GONE);
            }
        }
        return null;
    }

    @Override
    public Animator obtainOpenAnimator(@NonNull CircleMenu menu) {

        // action item 动画
        ObjectAnimator actionViewAnimator = null;
        if (menu.getActionItem() != null && menu.getActionItem().getView() != null) {
            View actionView = menu.getActionItem().getView();

            // 先缩小再放大
            PropertyValuesHolder[] holder1 = {
                    PropertyValuesHolder.ofFloat(View.SCALE_X, actionView.getScaleX(), 0, 1),
                    PropertyValuesHolder.ofFloat(View.SCALE_Y, actionView.getScaleY(), 0, 1),
            };
            actionViewAnimator = ObjectAnimator.ofPropertyValuesHolder(actionView, holder1);
            actionViewAnimator.setDuration(ANIMATION_DURATION);
        }

        // menu 动画
        ObjectAnimator menuAnimator = ObjectAnimator.ofPropertyValuesHolder(menu, PropertyValuesHolder.ofFloat(CircleMenu.MENU_RADIUS, menu.getMenuRadius(), menu.getMenuRadiusMax()));
        menuAnimator.setDuration(ANIMATION_DURATION);

        // item 动画
        List<Animator> animatorList = new ArrayList<>();
        if (menu.getActionItem() != null && menu.getActionItem().getView() != null && menu.getItems() != null) {
            CircleMenu.Item actionItem = menu.getActionItem();
            View actionView = actionItem.getView();

            float anchorX = actionView.getX() + actionView.getWidth() / 2;
            float anchorY = actionView.getY() + actionView.getHeight() / 2;
            float itemRadius = menu.getItemRadius();

            List<IMenu.IItem> items = menu.getItems();
            if (items != null && items.size() > 0) {
                RectF rectF = new RectF(
                        anchorX - itemRadius,
                        anchorY - itemRadius,
                        anchorX + itemRadius,
                        anchorY + itemRadius);
                Path path = new Path();
                path.addArc(rectF, menu.getStartAngle(), menu.getSweepAngle());
                PathMeasure measure = new PathMeasure(path, false);
                float distance = measure.getLength() / items.size();
                float[] pos = new float[2];

                long duration = items.size() > 3 ? 1500 / items.size() : ANIMATION_DURATION;
                for (int i = 0; i < items.size(); i++) {
                    // 计算 item 终点坐标
                    measure.getPosTan(distance * i, pos, null);

                    IMenu.IItem item = items.get(i);
                    View itemView = item.getView();

                    PropertyValuesHolder[] holders = {
                            PropertyValuesHolder.ofFloat(View.X, itemView.getX(), pos[0] - item.getWidth() / 2),
                            PropertyValuesHolder.ofFloat(View.Y, itemView.getY(), pos[1] - item.getHeight() / 2),
                            PropertyValuesHolder.ofFloat(View.SCALE_X, itemView.getScaleX(), 1),
                            PropertyValuesHolder.ofFloat(View.SCALE_Y, itemView.getScaleY(), 1),
                            PropertyValuesHolder.ofFloat(View.ALPHA, itemView.getAlpha(), 1),
                            PropertyValuesHolder.ofFloat(View.ROTATION, itemView.getRotation(), 360),
                    };

                    ObjectAnimator itemAnimator = ObjectAnimator.ofPropertyValuesHolder(itemView, holders);
                    itemAnimator.setDuration(duration);
                    itemAnimator.addListener(new AnimatorListenerAdapter() {

                        @Override
                        public void onAnimationStart(Animator animation) {
                            if (animation instanceof ObjectAnimator && ((ObjectAnimator) animation).getTarget() instanceof View) {
                                ((View) ((ObjectAnimator) animation).getTarget()).setVisibility(View.VISIBLE);
                            }
                        }
                    });
                    // 加入动画队列
                    animatorList.add(itemAnimator);
                }
            }
        }

        AnimatorSet animatorSet = new AnimatorSet();
        if (actionViewAnimator == null) {
            animatorSet.playTogether(menuAnimator);
        } else {
            animatorSet.playTogether(actionViewAnimator, menuAnimator);
        }
        animatorList.add(0, animatorSet);
        AnimatorSet openAnimatorSet = new AnimatorSet();
        openAnimatorSet.playSequentially(animatorList);
        return openAnimatorSet;
    }

    @Override
    public Animator obtainCloseAnimator(@NonNull CircleMenu menu) {
        List<Animator> animatorList = new ArrayList<>();

        // item 动画
        if (menu.getActionItem() != null && menu.getActionItem().getView() != null && menu.getItems() != null) {
            View actionView = menu.getActionItem().getView();

            float anchorX = actionView.getX();
            float anchorY = actionView.getY();

            List<IMenu.IItem> items = menu.getItems();
            long duration = items.size() > 3 ? 1500 / items.size() : ANIMATION_DURATION;
            if (items != null && items.size() > 0) {

                for (int i = 0; i < items.size(); i++) {
                    View itemView = items.get(i).getView();

                    PropertyValuesHolder[] holders = {
                            PropertyValuesHolder.ofFloat(View.X, itemView.getX(), anchorX),
                            PropertyValuesHolder.ofFloat(View.Y, itemView.getY(), anchorY),
                            PropertyValuesHolder.ofFloat(View.SCALE_X, itemView.getScaleX(), 0),
                            PropertyValuesHolder.ofFloat(View.SCALE_Y, itemView.getScaleY(), 0),
                            PropertyValuesHolder.ofFloat(View.ALPHA, itemView.getAlpha(), 0),
                            PropertyValuesHolder.ofFloat(View.ROTATION, itemView.getRotation(), 0),
                    };

                    ObjectAnimator itemAnimator = ObjectAnimator.ofPropertyValuesHolder(itemView, holders);
                    itemAnimator.setDuration(duration);
                    itemAnimator.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            if (animation instanceof ObjectAnimator && ((ObjectAnimator) animation).getTarget() instanceof View) {
                                ((View) ((ObjectAnimator) animation).getTarget()).setVisibility(View.GONE);
                            }
                        }
                    });
                    // 加入动画队列
                    animatorList.add(itemAnimator);
                }
            }
        }

        // menu 动画
        ObjectAnimator menuAnimator = ObjectAnimator.ofPropertyValuesHolder(menu, PropertyValuesHolder.ofFloat(CircleMenu.MENU_RADIUS, menu.getMenuRadius(), menu.getMenuRadiusMin()));
        menuAnimator.setDuration(ANIMATION_DURATION);

        // action item 动画
        ObjectAnimator actionViewAnimator = null;
        if (menu.getActionItem() != null && menu.getActionItem().getView() != null) {
            View actionView = menu.getActionItem().getView();

            // 先缩小再放大
            PropertyValuesHolder[] holder1 = {
                    PropertyValuesHolder.ofFloat(View.SCALE_X, actionView.getScaleX(), 0, 1),
                    PropertyValuesHolder.ofFloat(View.SCALE_Y, actionView.getScaleY(), 0, 1),
            };
            actionViewAnimator = ObjectAnimator.ofPropertyValuesHolder(actionView, holder1);
            actionViewAnimator.setDuration(ANIMATION_DURATION);
        }

        AnimatorSet animatorSet = new AnimatorSet();
        if (actionViewAnimator == null) {
            animatorSet.playTogether(menuAnimator);
        } else {
            animatorSet.playTogether(actionViewAnimator, menuAnimator);
        }
        animatorList.add(animatorSet);

        AnimatorSet closeAnimatorSet = new AnimatorSet();
        closeAnimatorSet.playSequentially(animatorList);
        return closeAnimatorSet;
    }
}
