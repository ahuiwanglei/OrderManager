package com.ordermanger.online.ordermanager.model;

import java.io.Serializable;

/**
 * Created by admin on 2017/6/8.
 */

public class OrderFilter  implements Serializable{
    private String CUSTNO;
    private String start_at;
    private String end_at;

    public String getCUSTNO() {
        return CUSTNO;
    }

    public void setCUSTNO(String CUSTNO) {
        this.CUSTNO = CUSTNO;
    }

    public String getStart_at() {
        return start_at;
    }

    public void setStart_at(String start_at) {
        this.start_at = start_at;
    }

    public String getEnd_at() {
        return end_at;
    }

    public void setEnd_at(String end_at) {
        this.end_at = end_at;
    }
}
