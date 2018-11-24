package com.loosu.floatingmenu;

import android.view.View;

public interface IMenu {
    public enum State {
        START_OPEN,
        START_CLOSE,
        SETTING,
        CLOSED,
        OPENED,
    }

    public void open(boolean animated);

    public void close(boolean animated);

    public interface IItem {
        public View getView();

        public void onMenuStateChange(IMenu menu, State state);
    }

    public interface OnStateChangeListener {
        public void onMenuStateChange(IMenu menu, State state);
    }
}
