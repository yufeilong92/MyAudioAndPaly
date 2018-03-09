package com.lawyee.myaudioandpaly.adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lawyee.myaudioandpaly.R;
import com.lawyee.myaudioandpaly.baidu.translate.TransApi;
import com.lawyee.myaudioandpaly.baidu.util.play;
import com.lawyee.myaudioandpaly.constant.AppSet;
import com.lawyee.myaudioandpaly.vo.SavePath;
import com.lawyee.myaudioandpaly.vo.TranslateResult;

import java.util.ArrayList;
import java.util.List;

/**
 * All rights Reserved, Designed By www.lawyee.com
 *
 * @version V 1.0 xxxxxxxx
 * @Title: MyAudioAndPaly
 * @Package com.lawyee.myaudioandpaly.adapter
 * @Description: $todo$
 * @author: YFL
 * @date: 2018/3/8 9:08
 * @verdescript 版本号 修改时间  修改人 修改的概要说明
 * @Copyright: 2018 www.lawyee.com Inc. All rights reserved.
 * 注意：本内容仅限于北京法意科技有限公司内部传阅，禁止外泄以及用于其他的商业目
 */
public class MainListAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList mDatas;

    public MainListAdapter(Context mContext, ArrayList list) {
        this.mContext = mContext;
        this.mDatas = list;

    }

    public void refreshData(ArrayList list) {
        this.mDatas = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas == null ? null : mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final SavePath savePath = (SavePath) mDatas.get(position);
        final ViewHolder mViewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_lsit, null);
            mViewHolder = new ViewHolder(convertView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        mViewHolder.mTvShowMsg.setText(savePath.getContent());
        mViewHolder.mImgPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play play = new play();
                play.playAudio(savePath.getPath());
            }
        });
        mViewHolder.mBtnTranslate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BindTranslateResult(mViewHolder, savePath.getContent());
            }

        });
        return convertView;
    }

    public void BindTranslateResult(final MainListAdapter.ViewHolder mViewHolder, String content) {
        MyAsync myAsync = new MyAsync();
        myAsync.setGetRescultContent(new MyAsync.callBlack() {
            @Override
            public void getRescult(String result) {

                if (result != null) {
                    mViewHolder.mTvContent.setVisibility(View.VISIBLE);
                    mViewHolder.mBtnTranslate.setClickable(false);
                    mViewHolder.mTvContent.setText(result);
                } else {
                    mViewHolder.mBtnTranslate.setClickable(true);
                }
            }
        });
        myAsync.execute(content);
    }

    static class MyAsync extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            TransApi api = new TransApi(AppSet.USERAPPID, AppSet.USERKEY);
            String transResult = api.getTransResult(strings[0], "en", "zh");
            return transResult;
        }

        @Override
        protected void onPostExecute(String s) {
            Gson gson = new Gson();
            TranslateResult translateResult = gson.fromJson(s, TranslateResult.class);
            List<TranslateResult.TransResultBean> trans_result = translateResult.getTrans_result();
            String dst = trans_result.get(0).getDst();
            if (getRescultContent != null) {
                getRescultContent.getRescult(dst);
            }
        }

        public interface callBlack {
            void getRescult(String result);
        }

        public callBlack getRescultContent;

        public void setGetRescultContent(callBlack getRescultContent) {
            this.getRescultContent = getRescultContent;
        }
    }

    public static class ViewHolder {
        public View rootView;
        public TextView mTvShowMsg;
        public ImageView mImgPlay;
        public ImageButton mBtnTranslate;
        public TextView mTvContent;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.mBtnTranslate = (ImageButton) rootView.findViewById(R.id.btn_translate);
            this.mTvShowMsg = (TextView) rootView.findViewById(R.id.tv_showMsg);
            this.mImgPlay = (ImageView) rootView.findViewById(R.id.img_play);
            this.mTvContent = (TextView) rootView.findViewById(R.id.tv_content);

        }

    }
}
