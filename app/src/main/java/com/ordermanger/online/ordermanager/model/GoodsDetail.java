package com.ordermanger.online.ordermanager.model;

import java.io.Serializable;

/**
 * Created by admin on 2017/5/21.
 */

public class GoodsDetail implements Serializable, Cloneable{
    private String GoodsNo;
    private String GoodsName;
    private String GoodsBarCode;
    private String GoodsPackQty;
    private String GoodsPackUnit;
    private String GoodsSellPrice;
    private String GoodsQty;
    private int GoodsOrderQty;
//    private String GoodsPrice = "未指定";
//
//    public String getGoodsPrice() {
//        return GoodsPrice;
//    }
//
//    public void setGoodsPrice(String goodsPrice) {
//        GoodsPrice = goodsPrice;
//    }

    public int getGoodsOrderQty() {
        return GoodsOrderQty;
    }

    public void setGoodsOrderQty(int goodsOrderQty) {
        GoodsOrderQty = goodsOrderQty;
    }

    public String getGoodsNo() {
        return GoodsNo;
    }

    public void setGoodsNo(String goodsNo) {
        GoodsNo = goodsNo;
    }

    public String getGoodsName() {
        return GoodsName;
    }

    public void setGoodsName(String goodsName) {
        GoodsName = goodsName;
    }

    public String getGoodsBarCode() {
        return GoodsBarCode;
    }

    public void setGoodsBarCode(String goodsBarCode) {
        GoodsBarCode = goodsBarCode;
    }

    public String getGoodsPackQty() {
        return GoodsPackQty;
    }

    public void setGoodsPackQty(String goodsPackQty) {
        GoodsPackQty = goodsPackQty;
    }

    public String getGoodsPackUnit() {
        return GoodsPackUnit;
    }

    public void setGoodsPackUnit(String goodsPackUnit) {
        GoodsPackUnit = goodsPackUnit;
    }

    public String getGoodsSellPrice() {
        return GoodsSellPrice;
    }

    public void setGoodsSellPrice(String goodsSellPrice) {
        GoodsSellPrice = goodsSellPrice;
    }

    public String getGoodsQty() {
        return GoodsQty;
    }

    public void setGoodsQty(String goodsQty) {
        GoodsQty = goodsQty;
    }

    @Override
    public String toString() {
        return "GoodsDetail{" +
                "GoodsNo='" + GoodsNo + '\'' +
                ", GoodsName='" + GoodsName + '\'' +
                ", GoodsBarCode='" + GoodsBarCode + '\'' +
                ", GoodsPackQty='" + GoodsPackQty + '\'' +
                ", GoodsPackUnit='" + GoodsPackUnit + '\'' +
                ", GoodsSellPrice='" + GoodsSellPrice + '\'' +
                ", GoodsQty='" + GoodsQty + '\'' +
                ", GoodsOrderQty=" + GoodsOrderQty +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null){
            return false;
        }else{
            if(obj instanceof GoodsDetail){
                GoodsDetail goodsDetail = (GoodsDetail)obj;
                if(goodsDetail.getGoodsNo().equals(this.getGoodsNo())){
                    return true;
                }
                return false;

            }else{
                return false;
            }
        }
    }

    public GoodsDetail copy(){
        try {
            return (GoodsDetail) clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return (GoodsDetail) super.clone();
    }

}
