package com.jsjn.apitest.dto;

/**
 * 注：由结算平台主动发起的通知采用此报文机构，如退款通知，文件就绪等。
 *
 * Created by yincongyang on 17/10/16.
 */
public class ReqBaseDTO {

    private String merchantId;
    private String subMerchantId;
    private String jsSeq;
    private String timestamp;
    private String transCode;
    private String notifyContent;
    private String digSign;
    private String version;
    private String reserved;

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getSubMerchantId() {
        return subMerchantId;
    }

    public void setSubMerchantId(String subMerchantId) {
        this.subMerchantId = subMerchantId;
    }

    public String getJsSeq() {
        return jsSeq;
    }

    public void setJsSeq(String jsSeq) {
        this.jsSeq = jsSeq;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getTransCode() {
        return transCode;
    }

    public void setTransCode(String transCode) {
        this.transCode = transCode;
    }

    public String getNotifyContent() {
        return notifyContent;
    }

    public void setNotifyContent(String notifyContent) {
        this.notifyContent = notifyContent;
    }

    public String getDigSign() {
        return digSign;
    }

    public void setDigSign(String digSign) {
        this.digSign = digSign;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getReserved() {
        return reserved;
    }

    public void setReserved(String reserved) {
        this.reserved = reserved;
    }
}
