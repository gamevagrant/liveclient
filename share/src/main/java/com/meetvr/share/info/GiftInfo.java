package com.meetvr.share.info;

import com.litesuits.orm.db.annotation.Table;

/**
 * Created by wzm-pc on 2016/9/1.
 */
@Table("datatable")
public class GiftInfo {
    private String id;
    private String gift_id;
    private int package_num;
    private String url2d;
    private float price;
    private String gift_name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGift_id() {
        return gift_id;
    }

    public void setGift_id(String gift_id) {
        this.gift_id = gift_id;
    }

    public int getPackage_num() {
        return package_num;
    }

    public void setPackage_num(int package_num) {
        this.package_num = package_num;
    }

    public String getUrl2d() {
        return url2d;
    }

    public void setUrl2d(String url2d) {
        this.url2d = url2d;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getGift_name() {
        return gift_name;
    }

    public void setGift_name(String gift_name) {
        this.gift_name = gift_name;
    }
}
