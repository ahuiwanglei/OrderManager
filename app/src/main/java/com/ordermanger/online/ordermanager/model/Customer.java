package com.ordermanger.online.ordermanager.model;

/**
 * Created by admin on 2017/6/8.
 */

public class Customer {
    private String CustNo;
    private String CustName;

    public String getCustNo() {
        return CustNo;
    }

    public void setCustNo(String custNo) {
        CustNo = custNo;
    }

    public String getCustName() {
        return CustName;
    }

    public void setCustName(String custName) {
        CustName = custName;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "CustNo='" + CustNo + '\'' +
                ", CustName='" + CustName + '\'' +
                '}';
    }
}
