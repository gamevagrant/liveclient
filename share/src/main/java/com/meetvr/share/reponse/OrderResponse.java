package com.meetvr.share.reponse;

import com.meetvr.share.control.vr_interface.BaseResponse;
import com.meetvr.share.utrils.Mutils;

import org.json.JSONObject;

/**
 * Created by wzm-pc on 2016/10/19.
 */

public class OrderResponse extends Response implements BaseResponse {

    private String order_id;
    private int mb_count ;
    private float money;
    private  String    pay_info;
    private String pay_way;

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public int getMb_count() {
        return mb_count;
    }

    public void setMb_count(int mb_count) {
        this.mb_count = mb_count;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public String getPay_info() {
        return pay_info;
    }

    public void setPay_info(String pay_info) {
        this.pay_info = pay_info;
    }

    public String getPay_way() {
        return pay_way;
    }

    public void setPay_way(String pay_way) {
        this.pay_way = pay_way;
    }

    @Override
    public void paseJson(JSONObject resp) {
       // Log.e("order",resp.toString());
        JSONObject jsonObject = parse(resp);
        if(jsonObject!=null&& Mutils.isResponseOk(getAct_stat())){
            order_id = Mutils.getJsonString(jsonObject,"order_id");
            mb_count = Mutils.getJsonInt(jsonObject,"mb_count");
            money =(float) Mutils.getJsonFloat(jsonObject,"money");
            pay_info = Mutils.getJsonString(jsonObject,"pay_info");
            pay_way = Mutils.getJsonString(jsonObject,"pay_way");
        }

    }

    @Override
    public String getAct_stat() {
        return super.getAct_stat();
    }

    @Override
    public String getErr_msg() {
        return super.getErr_msg();
    }

    @Override
    public int getErrCcode() {
        return super.getErr_code();
    }
}
