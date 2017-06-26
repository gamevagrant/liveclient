package com.meetvr.share.info;

import com.litesuits.orm.db.annotation.Column;
import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.annotation.Table;
import com.litesuits.orm.db.enums.AssignType;

/**
 * Created by wzm-pc on 2016/8/24.
 */
@Table("datatable")
public class UserInfo {

    // 设置为主键,自增
    @PrimaryKey(AssignType.AUTO_INCREMENT)
    // 取名为“_id”,如果此处不重新命名,就采用属性名称
    @Column("_id")
    public  int _id;

    private String id;
    private String usercode;
    private int usertype;
    private int acctype;  //0:表示手机注册用户 1:小米账户, 2:微信账户, 3:qq账户, 4:微博账号, 5:小米VR, 6:大鹏VR, 7:三星gear
    private String headphoto;
    private String username;
    private String sex;
    private String phone;
    private String remark;
    private String sig;
    private int    livetype;
    private String liveurl;
    private String roomid;
    private String token;
    private String qiniu_token;
    private int first_login;
    private int heartbeat;
    private String qiniu_file_domain;

    public String jsonValue;

    public String getQiniu_file_domain() {
        return qiniu_file_domain;
    }

    public void setQiniu_file_domain(String qiniu_file_domain) {
        this.qiniu_file_domain = qiniu_file_domain;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsercode() {
        return usercode;
    }

    public void setUsercode(String usercode) {
        this.usercode = usercode;
    }

    public int getUsertype() {
        return usertype;
    }

    public void setUsertype(int usertype) {
        this.usertype = usertype;
    }

    public void setAccountType(int type) {
        this.acctype = type;
    }
    public int getAccountType() {
        return acctype;
    }

    public String getHeadphoto() {
        return headphoto;
    }

    public void setHeadphoto(String headphoto) {
        this.headphoto = headphoto;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSig() {
        return sig;
    }

    public void setSig(String sig) {
        this.sig = sig;
    }

    public int getLivetype() {
        return livetype;
    }

    public void setLivetype(int livetype) {
        this.livetype = livetype;
    }

    public String getLiveurl() {
        return liveurl;
    }

    public void setLiveurl(String liveurl) {
        this.liveurl = liveurl;
    }

    public String getRoomid() {
        return roomid;
    }

    public void setRoomid(String roomid) {
        this.roomid = roomid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getQiniu_token() {
        return qiniu_token;
    }

    public void setQiniu_token(String qiniu_token) {
        this.qiniu_token = qiniu_token;
    }

    public int getFirst_login() {
        return first_login;
    }

    public void setFirst_login(int first_login) {
        this.first_login = first_login;
    }

    public int getHeartbeat() {
        return heartbeat;
    }

    public void setHeartbeat(int heartbeat) {
        this.heartbeat = heartbeat;
    }
}
