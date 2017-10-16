package com.jsjn.apitest.dto;

/**
 * 退款通知接口
 *
 * Created by yincongyang on 17/10/16.
 */
public class ReconciliationFileReadyDTO {
    private String transDate;
    private String fileName;
    private String reserved;

    public String getTransDate() {
        return transDate;
    }

    public void setTransDate(String transDate) {
        this.transDate = transDate;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getReserved() {
        return reserved;
    }

    public void setReserved(String reserved) {
        this.reserved = reserved;
    }
}
