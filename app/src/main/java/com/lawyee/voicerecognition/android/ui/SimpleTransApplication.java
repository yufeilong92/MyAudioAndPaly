package com.lawyee.voicerecognition.android.ui;

import android.app.Application;

/**
 * Created by fujiayi on 2017/10/18.
 */

public class SimpleTransApplication extends Application {
    private DigitalDialogInput digitalDialogInput;


    public com.lawyee.voicerecognition.android.ui.DigitalDialogInput getDigitalDialogInput() {
        return digitalDialogInput;
    }

    public void setDigitalDialogInput(com.lawyee.voicerecognition.android.ui.DigitalDialogInput digitalDialogInput) {
        this.digitalDialogInput = digitalDialogInput;
    }
}
