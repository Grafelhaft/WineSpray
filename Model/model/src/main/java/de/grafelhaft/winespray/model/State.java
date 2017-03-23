package de.grafelhaft.winespray.model;

/**
 * Created by Markus on 09.10.2016.
 */

public interface State {

    int NEW = 0;
    int ACTIVE = 1;
    int PAUSED = 2;
    int STOPPED = 3;

    interface OnStateChangedListener {
        void onStateChanged(int state);
    }
}
