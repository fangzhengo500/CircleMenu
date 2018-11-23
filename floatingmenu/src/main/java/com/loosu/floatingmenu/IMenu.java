package com.loosu.floatingmenu;

public interface IMenu {
    public enum State {
        START_OPEN,
        START_CLOSE,
        CLOSING,
        OPENING,
        CLOSED,
        OPENED,
    }

    public void open(boolean animated);

    public void close(boolean animated);

    public interface OnStateChangeListener {
        public void onStatteChange(IMenu menu, State state);
    }
}
