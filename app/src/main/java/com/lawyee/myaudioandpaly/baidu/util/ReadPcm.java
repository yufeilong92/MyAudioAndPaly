package com.lawyee.myaudioandpaly.baidu.util;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * All rights Reserved, Designed By www.lawyee.com
 *
 * @version V 1.0 xxxxxxxx
 * @Title: MyAudioAndPaly
 * @Package com.lawyee.myaudioandpaly.baidu.util
 * @Description: 播放pcm语音
 * @author: YFL
 * @date: 2018/3/8 15:18
 * @verdescript 版本号 修改时间  修改人 修改的概要说明
 * @Copyright: 2018 www.lawyee.com Inc. All rights reserved.
 * 注意：本内容仅限于北京法意科技有限公司内部传阅，禁止外泄以及用于其他的商业目
 */
public class ReadPcm {
    public void playPcm(String name) {
        File file = new File(name);
        if (file == null) {
            return;
        }
        //读取文件

        int musicLength = (int) (file.length() / 2);
        short[] music = new short[musicLength];
        try {
            InputStream is = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(is);
            DataInputStream dis = new DataInputStream(bis);
            /**
             * AudioFormat.CHANNEL_CONFIGURATION_STEREO //双声道
             * AudioFormat.CHANNEL_CONFIGURATION_MONO 单声道
             *
             */
             int sampleRateInHz=16000;
            AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
                    sampleRateInHz, AudioFormat.CHANNEL_CONFIGURATION_MONO,
                    AudioFormat.ENCODING_PCM_16BIT,
                    musicLength,
                    AudioTrack.MODE_STREAM);
            audioTrack.play();

            int i = 0;
            while (dis.available() > 0) {
                music[i] = dis.readShort();
                i++;
            }
            dis.close();
            audioTrack.write(music, 0, musicLength);
            audioTrack.stop();
        } catch (Throwable t) {
            Log.e("YFL", "播放失败");
        }
    }
}
