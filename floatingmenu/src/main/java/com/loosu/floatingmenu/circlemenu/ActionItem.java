package com.loosu.floatingmenu.circlemenu;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.view.View;

import com.loosu.floatingmenu.IMenu;

public class ActionItem extends BaseItem implements IActionView {

    public ActionItem(View view) {
        super(view);
    }

    @Override
    public void onMenuStateChange(IMenu menu, IMenu.State state) {
        switch (state) {
            case OPENED:
            case START_CLOSE:
                startAnimation();
                break;
        }
    }

    private void startAnimation() {
        PropertyValuesHolder[] holders1 = {
                PropertyValuesHolder.ofFloat(View.SCALE_X, mView.getScaleX(), 0),
                PropertyValuesHolder.ofFloat(View.SCALE_Y, mView.getScaleY(), 0),
        };
        ObjectAnimator animator1 = ObjectAnimator.ofPropertyValuesHolder(mView, holders1);

        PropertyValuesHolder[] holders2 = {
                PropertyValuesHolder.ofFloat(View.SCALE_X, 0, 1),
                PropertyValuesHolder.ofFloat(View.SCALE_Y, 0, 1),
        };
        ObjectAnimator animator2 = ObjectAnimator.ofPropertyValuesHolder(mView, holders2);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animator1, animator2);
        animatorSet.start();
    }
}
