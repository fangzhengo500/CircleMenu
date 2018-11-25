package com.loosu.floatingmenu.circlemenu;

import android.view.View;

import com.loosu.floatingmenu.IMenu;


public class BaseItem implements IMenu.IItem {
    protected final View mView;
    private int mWidth;
    private int mHeight;

    public BaseItem(View view, int width, int height) {
        mView = view;
        mWidth = width;
        mHeight = height;
    }

    @Override
    public int getWidht() {
        return mWidth;
    }

    @Override
    public int getHeight() {
        return mHeight;
    }

    @Override
    public View getView() {
        return mView;
    }
}
