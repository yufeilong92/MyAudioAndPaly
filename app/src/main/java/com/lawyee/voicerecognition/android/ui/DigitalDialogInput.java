package com.lawyee.voicerecognition.android.ui;


import com.lawyee.myaudioandpaly.baidu.control.MyRecognizer;
import com.lawyee.myaudioandpaly.baidu.recognization.ChainRecogListener;

import java.util.Map;

/**
 * Created by fujiayi on 2017/10/18.
 */

public class DigitalDialogInput {
    private MyRecognizer myRecognizer;

    private ChainRecogListener listener;

    private int code;

    private Map<String, Object> startParams;

    public DigitalDialogInput(MyRecognizer myRecognizer, ChainRecogListener listener, Map<String, Object> startParams) {
        this.myRecognizer = myRecognizer;
        this.listener = listener;
        this.startParams = startParams;
    }

    public MyRecognizer getMyRecognizer() {
        return myRecognizer;
    }

    public ChainRecogListener getListener() {
        return listener;
    }

    public Map<String, Object> getStartParams() {
        return startParams;
    }
}
