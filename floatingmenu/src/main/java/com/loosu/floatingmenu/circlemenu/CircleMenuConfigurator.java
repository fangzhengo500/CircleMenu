package com.loosu.floatingmenu.circlemenu;

import android.content.Context;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Point;
import android.graphics.RectF;
import android.icu.util.Measure;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;

import com.loosu.floatingmenu.IMenu;

import java.util.ArrayList;
import java.util.List;

public class CircleMenuConfigurator {

    private final Context mContext;
    private CircleMenu.Item mActionItem;
    private List<CircleMenu.Item> mItems = new ArrayList<>();

    private CircleMenu.Anchor mAnchor;
    private int mAnchorOffsetX;
    private int mAnchorOffsetY;

    public CircleMenuConfigurator(Context context) {
        mContext = context.getApplicationContext();
    }

    public CircleMenuConfigurator setActionItem(int layoutId, int width, int height) {
        View actionView = LayoutInflater.from(mContext).inflate(layoutId, null, false);
        mActionItem = new CircleMenu.Item(actionView, width, height);
        return this;
    }

    public CircleMenuConfigurator setActionItem(View actionView, int width, int height) {
        mActionItem = new CircleMenu.Item(actionView, width, height);
        return this;
    }

    public CircleMenuConfigurator setActionItem(CircleMenu.Item actionItem) {
        mActionItem = actionItem;
        return this;
    }

    public CircleMenuConfigurator addMenuItem(int layoutId, int width, int height) {
        View menuView = LayoutInflater.from(mContext).inflate(layoutId, null, false);
        mItems.add(new CircleMenu.Item(menuView, width, height));
        return this;
    }

    public CircleMenuConfigurator addMenuItem(View menuView, int width, int height) {
        mItems.add(new CircleMenu.Item(menuView, width, height));
        return this;
    }

    public CircleMenuConfigurator addMenuItem(CircleMenu.Item menuItem) {
        mItems.add(menuItem);
        return this;
    }

    public CircleMenuConfigurator setAnchor(CircleMenu.Anchor anchor) {
        mAnchor = anchor;
        return this;
    }

    public CircleMenuConfigurator setAnchorOffsetX(int anchorOffsetX) {
        mAnchorOffsetX = anchorOffsetX;
        return this;
    }

    public CircleMenuConfigurator setAnchorOffsetY(int anchorOffsetY) {
        mAnchorOffsetY = anchorOffsetY;
        return this;
    }

    public void applyTo(CircleMenu menu) {
        menu.setActionItem(mActionItem);
        menu.setItems(mItems);
        menu.setAnchor(mAnchor);
        menu.setAnchorOffsetX(mAnchorOffsetX);
        menu.setAnchorOffsetY(mAnchorOffsetY);

        CircleMenu.Item actionItem = menu.getActionItem();
        if (actionItem == null) {
            return;
        }

        int maxItemSize = 0;
        List<? extends IMenu.IItem> items = menu.getItems();
        if (items != null) {
            for (IMenu.IItem item : items) {
                maxItemSize = Math.max(maxItemSize, Math.max(item.getWidth(), item.getHeight()));
            }
        }
//
//        float itemRadius = .8f * (menu.getMenuRadiusMax() - maxItemSize);
//
//        Point anchorPoint = menu.getAnchorPoint();
//        float startAngle = 0;
//        float endAngle = 0;
//
//        switch (menu.getAnchor()) {
//            case LEFT_TOP:
//                startAngle = (float) Math.atan2(anchorPoint.y, Math.sqrt(Math.pow(itemRadius, 2) - Math.pow(anchorPoint.y, 2)));
//                endAngle = (float) Math.atan2(Math.sqrt(Math.pow(itemRadius, 2) - Math.pow(anchorPoint.y, 2)), -anchorPoint.x);
//                break;
//        }
    }
}
