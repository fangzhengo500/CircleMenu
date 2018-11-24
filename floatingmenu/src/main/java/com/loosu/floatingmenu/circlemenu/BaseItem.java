package com.loosu.floatingmenu.circlemenu;

import android.view.View;

import com.loosu.floatingmenu.IMenu;


public abstract class BaseItem implements IMenu.IItem {
    protected final View mView;

    public BaseItem(View view) {
        mView = view;
    }

    @Override
    public View getView() {
        return mView;
    }
}
