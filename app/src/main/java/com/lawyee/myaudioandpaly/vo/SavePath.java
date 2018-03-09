package com.lawyee.myaudioandpaly.vo;

import java.util.ArrayList;

/**
 * All rights Reserved, Designed By www.lawyee.com
 *
 * @version V 1.0 xxxxxxxx
 * @Title: MyAudioAndPaly
 * @Package com.lawyee.myaudioandpaly.vo
 * @Description: $todo$
 * @author: YFL
 * @date: 2018/3/8 15:02
 * @verdescript 版本号 修改时间  修改人 修改的概要说明
 * @Copyright: 2018 www.lawyee.com Inc. All rights reserved.
 * 注意：本内容仅限于北京法意科技有限公司内部传阅，禁止外泄以及用于其他的商业目
 */
public class SavePath {

    private ArrayList<SavePath> getPath;


    private String id;
    private String content;
    private String path;
    public ArrayList<SavePath> getGetPath() {
        return getPath;
    }

    public void setGetPath(ArrayList<SavePath> getPath) {
        this.getPath = getPath;
    }
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }


}
