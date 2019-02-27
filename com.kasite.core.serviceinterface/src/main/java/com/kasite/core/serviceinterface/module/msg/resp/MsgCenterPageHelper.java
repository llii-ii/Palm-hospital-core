package com.kasite.core.serviceinterface.module.msg.resp;
import java.util.ArrayList;
import java.util.List;

import com.kasite.core.common.resp.AbsResp;
public class MsgCenterPageHelper<T> extends AbsResp{
	 //实体类集合
    private List<T> rows = new ArrayList<T>();
    //数据总条数
    private int total;
 
    public MsgCenterPageHelper() {
        super();
    }
 
    public List<T> getRows() {
        return rows;
    }
 
    public void setRows(List<T> rows) {
        this.rows = rows;
    }
 
    public int getTotal() {
        return total;
    }
 
    public void setTotal(int total) {
        this.total = total;
    }
}
