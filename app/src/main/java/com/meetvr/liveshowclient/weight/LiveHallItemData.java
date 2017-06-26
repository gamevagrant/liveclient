package com.meetvr.liveshowclient.weight;

import com.litesuits.orm.db.annotation.Table;

/**
 * Created by wzm-pc on 2016/10/14.
 */
@Table("datatable")
public class LiveHallItemData extends Entity {
    public long live_id;
    //int	直播记录主键
    String live_stat;//	String	类型 living(直播) /vod(回放)
    String host_id;//	String	主播ID
    //String user_code;//	String	主播账号
    String head_pic;//	String	主播头像
    String live_show_url;//	String	直播封面
    String nickname;//	String	主播昵称
    String sex;//	String	主播性别  F 女 M 男
    String live_url;//	String	直播url/回放url
    String room_id;//	String	腾讯聊天室id
    String living_count	;//String	当前在线人数，直播显示
    String view_count;//	String	总浏览人数，回放显示
    String content;//	String	主播介绍

  public   String jsonValue="";

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getLive_url() {
        return live_url;
    }

    public void setLive_url(String live_url) {
        this.live_url = live_url;
    }

    public String getRoom_id() {
        return host_id;//room_id;
    }

    public void setRoom_id(String room_id) {
        /*this.room_id*/host_id = room_id;
    }
    /*
        public String getLiving_count() {
            return living_count;
        }

        public void setLiving_count(String living_count) {
            this.living_count = living_count;
        }
    */
    public String getView_count() {
        return view_count;
    }

    public void setView_count(String view_count) {
        this.view_count = view_count;
    }

    public long getId() {
        return live_id;
    }

    public void setId(int id) {
        this.live_id = id;
        this.id = id;
    }

    public String getType() {
        return live_stat;
    }

    public void setType(String type) {
        this.live_stat = type;
    }

    public String getUser_id() {
        return host_id;
    }

    public void setUser_id(String user_id) {
        this.host_id = user_id;
    }
/*
    public String getUser_code() {
        return user_code;
    }

    public void setUser_code(String user_code) {
        this.user_code = user_code;
    }
*/
    public String getHead_pic() {
        return head_pic;
    }

    public void setHead_pic(String head_pic) {
        this.head_pic = head_pic;
    }

    public String getRoom_show_pic() {
        return live_show_url;
    }

    public void setRoom_show_pic(String room_show_pic) {
        this.live_show_url = room_show_pic;
    }
}
