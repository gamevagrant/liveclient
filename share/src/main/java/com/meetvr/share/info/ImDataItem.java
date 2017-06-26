package com.meetvr.share.info;

/**
 * Created by wzm-pc on 2016/8/29.
 */
public class ImDataItem {
    private int msgtype;  //内容类型 0 文本  1 通知  2 送礼
    private String nickName;
    private String content;
    private String gilfId;
    private String gilfUrl;
    private String gilfNum;
    private String userId;

    public int getMsgtype() {
        return msgtype;
    }

    public void setMsgtype(int msgtype) {
        this.msgtype = msgtype;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getGilfId() {
        return gilfId;
    }

    public void setGilfId(String gilfId) {
        this.gilfId = gilfId;
    }

    public String getGilfUrl() {
        return gilfUrl;
    }

    public void setGilfUrl(String gilfUrl) {
        this.gilfUrl = gilfUrl;
    }

    public String getGilfNum() {
        return gilfNum;
    }

    public void setGilfNum(String gilfNum) {
        this.gilfNum = gilfNum;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
