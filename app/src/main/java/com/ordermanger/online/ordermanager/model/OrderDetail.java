package com.ordermanger.online.ordermanager.model;

import java.io.Serializable;

/**
 * Created by admin on 2017/5/21.
 */

public class OrderDetail implements Serializable{
    private  String LocNo;
    private String OwnerNo;
    private String LocName;
    private String OwnerName;
    private String SheetID;
    private String CustomerNo;
    private String CustomerName;
    private String SHEETDATE;
    private String Memo;
    private String SheetType;
    private String DeptNo;
    private String DeptName;
    private String GoodsSellPrice;

    public String getDeptName() {
        return DeptName;
    }

    public void setDeptName(String deptName) {
        DeptName = deptName;
    }

    public String getGoodsSellPrice() {
        return GoodsSellPrice;
    }

    public void setGoodsSellPrice(String goodsSellPrice) {
        GoodsSellPrice = goodsSellPrice;
    }

    public String getLocName() {
        return LocName;
    }

    public void setLocName(String locName) {
        LocName = locName;
    }

    public String getOwnerName() {
        return OwnerName;
    }

    public void setOwnerName(String ownerName) {
        OwnerName = ownerName;
    }

    public String getLocNo() {
        return LocNo;
    }

    public void setLocNo(String locNo) {
        LocNo = locNo;
    }

    public String getOwnerNo() {
        return OwnerNo;
    }

    public void setOwnerNo(String ownerNo) {
        OwnerNo = ownerNo;
    }

    public String getSheetID() {
        return SheetID;
    }

    public void setSheetID(String sheetID) {
        SheetID = sheetID;
    }

    public String getCustomerNo() {
        return CustomerNo;
    }

    public void setCustomerNo(String customerNo) {
        CustomerNo = customerNo;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public String getSHEETDATE() {
        return SHEETDATE;
    }

    public void setSHEETDATE(String SHEETDATE) {
        this.SHEETDATE = SHEETDATE;
    }

    public String getMemo() {
        return Memo;
    }

    public void setMemo(String memo) {
        Memo = memo;
    }

    public String getSheetType() {
        return SheetType;
    }

    public void setSheetType(String sheetType) {
        SheetType = sheetType;
    }

    public String getDeptNo() {
        return DeptNo;
    }

    public void setDeptNo(String deptNo) {
        DeptNo = deptNo;
    }
}
