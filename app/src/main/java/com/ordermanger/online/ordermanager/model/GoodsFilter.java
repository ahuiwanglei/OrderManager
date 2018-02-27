package com.ordermanger.online.ordermanager.model;

import java.io.Serializable;

/**
 * Created by admin on 2017/6/15.
 */

public class GoodsFilter implements Serializable {

    private String goodsNo;
    private String goodsName;
    private String goodsBarCode;

    public String getGoodsNo() {
        return goodsNo;
    }

    public void setGoodsNo(String goodsNo) {
        this.goodsNo = goodsNo;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsBarCode() {
        return goodsBarCode;
    }

    public void setGoodsBarCode(String goodsBarCode) {
        this.goodsBarCode = goodsBarCode;
    }
}
