package com.meetvr.share.info;

/**
 * Created by wzm-pc on 2016/10/19.
 */

public class RcardInfo {
    private int id;
    private int mb_num;
    private int  discount_ratio;
    private int  discount_mb_num;
    private float price;
    private  String  card_pic;
    private String card_name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMb_num() {
        return mb_num;
    }

    public void setMb_num(int mb_num) {
        this.mb_num = mb_num;
    }

    public int getDiscount_ratio() {
        return discount_ratio;
    }

    public void setDiscount_ratio(int discount_ratio) {
        this.discount_ratio = discount_ratio;
    }

    public int getDiscount_mb_num() {
        return discount_mb_num;
    }

    public void setDiscount_mb_num(int discount_mb_num) {
        this.discount_mb_num = discount_mb_num;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getCard_pic() {
        return card_pic;
    }

    public void setCard_pic(String card_pic) {
        this.card_pic = card_pic;
    }

    public String getCard_name() {
        return card_name;
    }

    public void setCard_name(String card_name) {
        this.card_name = card_name;
    }
}
