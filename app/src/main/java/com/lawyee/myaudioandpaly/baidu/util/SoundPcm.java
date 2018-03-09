package com.lawyee.myaudioandpaly.baidu.util;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * All rights Reserved, Designed By www.lawyee.com
 *
 * @version V 1.0 xxxxxxxx
 * @Title: MyAudioAndPaly
 * @Package com.lawyee.myaudioandpaly.baidu.util
 * @Description: 录制pcm格式录音
 * @author: YFL
 * @date: 2018/3/9 11:36
 * @verdescript 版本号 修改时间  修改人 修改的概要说明
 * @Copyright: 2018 www.lawyee.com Inc. All rights reserved.
 * 注意：本内容仅限于北京法意科技有限公司内部传阅，禁止外泄以及用于其他的商业目
 */
public class SoundPcm {
    Context context;

    public SoundPcm(Context context) {
        this.context = context;
    }

    private boolean isRecording;

    private File file;
    public static final String TAG = "SoundPcm.class";

    public void setIsRecording(boolean isRecording) {
        this.isRecording = isRecording;
    }

    public String  startSoundPcm() {
        initSamplePath(context);
        //16K采集率
        int frequency = 16000;
        //格式
        /**
         * 双声道
         */
        int channelConfiguration = AudioFormat.CHANNEL_CONFIGURATION_STEREO;
        /**
         * 单声道
         */
//        int channelConfiguration = AudioFormat.CHANNEL_CONFIGURATION_MONO;
        //16Bit
        int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;
        String path = samplePath + "/" + FileUtil.Random6() + ".pcm";
        //生成PCM文件
        file = new File(path);
        Log.i(TAG, "生成文件");
        //如果存在，就先删除再创建
        if (file.exists())
            file.delete();
        Log.i(TAG, "删除文件");
        try {
            file.createNewFile();
            Log.i(TAG, "创建文件");
        } catch (IOException e) {
            Log.i(TAG, "未能创建");
            throw new IllegalStateException("未能创建" + file.toString());
        }
        try {
            //输出流
            OutputStream os = new FileOutputStream(file);
            BufferedOutputStream bos = new BufferedOutputStream(os);
            DataOutputStream dos = new DataOutputStream(bos);
            int bufferSize = AudioRecord.getMinBufferSize(frequency, channelConfiguration, audioEncoding);
            AudioRecord audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, frequency, channelConfiguration, audioEncoding, bufferSize);

            short[] buffer = new short[bufferSize];
            audioRecord.startRecording();
            Log.i(TAG, "开始录音");
            isRecording = true;
            while (isRecording) {
                int bufferReadResult = audioRecord.read(buffer, 0, bufferSize);
                for (int i = 0; i < bufferReadResult; i++) {
                    dos.writeShort(buffer[i]);
                }
            }
            audioRecord.stop();
            dos.close();
        } catch (Throwable t) {
            Log.e(TAG, "录音失败");
        }
        return path;
    }

    public void deleFile() {
        if (file == null) {
            return;
        }
        file.delete();
    }
    protected String samplePath;

    protected void initSamplePath(Context context) {
        String sampleDir = "baiduASR";
         samplePath = Environment.getExternalStorageDirectory().toString() + "/" + sampleDir;
        if (!FileUtil.makeDir(samplePath)) {
            samplePath = context.getExternalFilesDir(sampleDir).getAbsolutePath();
            if (!FileUtil.makeDir(samplePath)) {
                throw new RuntimeException("创建临时目录失败 :" + samplePath);
            }
        }
    }
}
