package com.lawyee.myaudioandpaly.baidu.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.baidu.speech.asr.SpeechConstant;
import com.lawyee.myaudioandpaly.baidu.activity.setting.OnlineSetting;
import com.lawyee.myaudioandpaly.baidu.control.MyRecognizer;
import com.lawyee.myaudioandpaly.baidu.recognization.ChainRecogListener;
import com.lawyee.myaudioandpaly.baidu.recognization.CommonRecogParams;
import com.lawyee.myaudioandpaly.baidu.recognization.MessageStatusRecogListener;
import com.lawyee.myaudioandpaly.baidu.recognization.offline.OfflineRecogParams;
import com.lawyee.myaudioandpaly.baidu.recognization.online.OnlineRecogParams;
import com.lawyee.myaudioandpaly.baidu.util.Logger;
import com.lawyee.myaudioandpaly.vo.SavePath;
import com.lawyee.voicerecognition.android.ui.BaiduASRDigitalDialog;
import com.lawyee.voicerecognition.android.ui.DigitalDialogInput;
import com.lawyee.voicerecognition.android.ui.SimpleTransApplication;

import java.util.ArrayList;
import java.util.Map;


/**
 * UI 界面调用
 * <p>
 * 本类仅仅初始化及释放MyRecognizer，具体识别逻辑在BaiduASRDialog。对话框UI在BaiduASRDigitalDialog
 * 依赖SimpleTransApplication 在两个activity中传递输入参数
 * <p>
 * <p>
 * <p>
 * Created by fujiayi on 2017/10/17.
 */

public class ActivityUiDialog extends ActivityRecog {

//    {
//        descText = "多了UI 对话框。使用在线普通识别功能(含长语音)\n"
//                + "请先测试“在线识别”界面\n"
//                + "识别逻辑在BaiduASRDialog类\n"
//                + "\n"
//                + "集成指南：\n"
//                + "相关资源文件：src/resources/*.properites。 src/res 各个目录下以bdsppech_开头的资源文件名\n"
//                + "1. 测试“在线识别“，查看参数和回调有问题么\n"
//                + "2. 同样的输入参数，使用“UI”，查看回调有问题么\n"
//                + "3. 查看界面及功能是否正常\n\n";
//    }

    /**
     * 对话框界面的输入参数
     */
    private DigitalDialogInput input;
    /**
     * 有2个listner，一个是用户自己的业务逻辑，如MessageStatusRecogListener。另一个是UI对话框的。
     * 使用这个ChainRecogListener把两个listener和并在一起
     */
    private ChainRecogListener listener;

    private static String TAG = "ActivityUiDialog";
    private String path;

    public ActivityUiDialog() {
        super();
        settingActivityClass = OnlineSetting.class;
    }

    /**
     * 在onCreate中调用。初始化识别控制类MyRecognizer
     */
    protected void initRecog() {
        listener = new ChainRecogListener();
        // DigitalDialogInput 输入 ，MessageStatusRecogListener可替换为用户自己业务逻辑的listener
        listener.addListener(new MessageStatusRecogListener(handler));
        myRecognizer = new MyRecognizer(this, listener); // DigitalDialogInput 输入
        apiParams = getApiParams();
        status = STATUS_NONE;
        if (enableOffline) {
            myRecognizer.loadOfflineEngine(OfflineRecogParams.fetchOfflineParams());
        }
    }

    /**
     * 开始录音，点击“开始”按钮后调用。
     */
    @Override
    protected void start() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        Map<String, Object> params = apiParams.fetch(sp);  // params可以手动填入
        params.put("_outfile", true);
        params.put("_tips_sound", true);
        path = (String) params.get(SpeechConstant.OUT_FILE);
        // BaiduASRDigitalDialog的输入参数
        input = new DigitalDialogInput(myRecognizer, listener, params);

        Intent intent = new Intent(this, BaiduASRDigitalDialog.class);
        // 在BaiduASRDialog中读取
        ((SimpleTransApplication) getApplicationContext()).setDigitalDialogInput(input);

        // 修改对话框样式
//         intent.putExtra(BaiduASRDigitalDialog.PARAM_DIALOG_THEME, BaiduASRDigitalDialog.THEME_ORANGE_DEEPBG);

        running = true;
        startActivityForResult(intent, 2);

    }

    @Override
    protected CommonRecogParams getApiParams() {
        return new OnlineRecogParams(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        running = false;
        Log.i(TAG, "requestCode" + requestCode);
        String result="";
        if (requestCode == 2) {
            String message = "对话框的识别结果：";
            if (resultCode == RESULT_OK) {
                ArrayList results = data.getStringArrayListExtra("results");
                if (results != null && results.size() > 0) {
                    message += results.get(0);
                    result= String.valueOf(results.get(0));
                }
            } else {
                message += "没有结果";
            }
            Logger.info(message);
            SavePath savePath = new SavePath();
            savePath.setPath(path);
            savePath.setContent(result);
            sendMessage(savePath);
            Toast.makeText(ActivityUiDialog.this, "" + message, Toast.LENGTH_SHORT).show();
            mBtnPlay.setText("开始录音");
            mBtnPlay.setEnabled(true);
            mImgSetting.setEnabled(true);
        }

    }

    @Override
    protected void onPause() {
        Log.i(TAG, "onPause");
        super.onPause();
        if (!running) {
            myRecognizer.release();
            finish();
        }
    }
}