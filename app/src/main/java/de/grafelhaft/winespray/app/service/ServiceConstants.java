package de.grafelhaft.winespray.app.service;

/**
 * Created by Markus on 11.09.2016.
 */
public interface ServiceConstants {

    interface ACTION {
        String START_FOREGROUND_ACTION = "de.grafelhaft.wine.carlowitz.action.startforegroundservice";
        String STOP_FOREGROUND_ACTION = "de.grafelhaft.wine.carlowitz.action.stopforegroundservice";
        String PAUSE_FOREGROUND_ACTION = "de.grafelhaft.wine.carlowitz.action.pauseforegroundservice";

        String MAIN_ACTION = "de.grafelhaft.wine.carlowitz.action.mainaction";
    }

    interface NOTIFICATION_ID {
        int FOREGROUND_SERVICE = 51;
    }
}
