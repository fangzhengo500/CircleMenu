package com.loosu.floatingmenu.circlemenu;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.view.View;

import com.loosu.floatingmenu.IMenu;

import java.util.ArrayList;
import java.util.List;

public class CircleMenuAnimatorAdapter implements IMenu.IAnimatedAdapter<CircleMenu> {

    @Override
    public Animator obtainOpenAnimator(CircleMenu menu) {

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
        }

        // menu 动画
        ObjectAnimator menuAnimator = ObjectAnimator.ofPropertyValuesHolder(menu, PropertyValuesHolder.ofFloat(CircleMenu.RADIUS, menu.getRadius(), menu.getRadiusMax()));
        menuAnimator.setDuration(200);

        // item 动画
        List<Animator> animatorList = new ArrayList<>();
        if (menu.getActionItem() != null && menu.getActionItem().getView() != null && menu.getItems() != null) {
            View actionView = menu.getActionItem().getView();

            float anchorX = actionView.getX();
            float anchorY = actionView.getY();
            float radiusMax = menu.getRadiusMax();

            List<IMenu.IItem> items = menu.getItems();
            if (items != null && items.size() > 0) {

                RectF rectF = new RectF(
                        anchorX - radiusMax + 50,
                        anchorY - radiusMax + 50,
                        anchorX + radiusMax - 50,
                        anchorY + radiusMax - 50);
                Path path = new Path();
                path.addArc(rectF, menu.getStartAngle(), menu.getSweepAngle());
                PathMeasure measure = new PathMeasure(path, false);
                float distance = measure.getLength() / items.size();
                float[] pos = new float[2];

                for (int i = 0; i < items.size(); i++) {
                    // 计算 item 终点坐标
                    measure.getPosTan(distance * i, pos, null);

                    View itemView = items.get(i).getView();

                    PropertyValuesHolder[] holders = {
                            PropertyValuesHolder.ofFloat(View.X, itemView.getX(), pos[0]),
                            PropertyValuesHolder.ofFloat(View.Y, itemView.getY(), pos[1]),
                            PropertyValuesHolder.ofFloat(View.SCALE_X, itemView.getScaleX(), 1),
                            PropertyValuesHolder.ofFloat(View.SCALE_Y, itemView.getScaleY(), 1),
                            PropertyValuesHolder.ofFloat(View.ALPHA, itemView.getAlpha(), 1),
                            PropertyValuesHolder.ofFloat(View.ROTATION, itemView.getRotation(), 360),
                    };

                    ObjectAnimator itemAnimator = ObjectAnimator.ofPropertyValuesHolder(itemView, holders);
                    itemAnimator.setDuration(300);
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
    public Animator obtainCloseAnimator(CircleMenu menu) {
        List<Animator> animatorList = new ArrayList<>();

        // item 动画
        if (menu.getActionItem() != null && menu.getActionItem().getView() != null && menu.getItems() != null) {
            View actionView = menu.getActionItem().getView();

            float anchorX = actionView.getX();
            float anchorY = actionView.getY();

            List<IMenu.IItem> items = menu.getItems();
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
                    itemAnimator.setDuration(300);
                    // 加入动画队列
                    animatorList.add(itemAnimator);
                }
            }
        }

        // menu 动画
        ObjectAnimator menuAnimator = ObjectAnimator.ofPropertyValuesHolder(menu, PropertyValuesHolder.ofFloat(CircleMenu.RADIUS, menu.getRadius(), menu.getRadiusMin()));
        menuAnimator.setDuration(200);

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
