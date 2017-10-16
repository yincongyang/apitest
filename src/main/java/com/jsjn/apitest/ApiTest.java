package com.jsjn.apitest;

import com.google.gson.Gson;
import com.jsjn.apitest.dto.JNReqHeaderDTO;
import com.jsjn.apitest.dto.ReconciliationFileReadyDTO;
import com.jsjn.apitest.dto.ReqBaseDTO;
import com.jsjn.apitest.util.DigestUtil;
import com.jsjn.apitest.util.RSAUtil;
import com.jsjn.apitest.util.Keys;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.Date;

/**
 * 对账文件就绪通知接口示例
 * Created by yincongyang on 17/10/16.
 */
public class ApiTest {

    public static void main(String[] args) throws Exception {
        System.out.println("构造请求报文...");

        Gson gson = new Gson();

        //组装业务字段
        String str1 = gson.toJson(buildReconciliationFileReady());
        System.out.println("业务字段： " + str1);

        //加上结算平台通用报文头
        String str2 = gson.toJson(buildReqBase(str1));
        System.out.println("业务报文： " + str2);

        //加上金农API平台报文头
        JNReqHeaderDTO reqDto = buildJNReqHeader(str2);
        String str3 = gson.toJson(reqDto);
        System.out.println("未加签报文： " + str3);

        //获取待签名字符串
        String url = "/v1/custom/reconciliation_file_ready_notify";
        String sourceStr = DigestUtil.buildSourceStr("POST",url,reqDto,"signature");
        System.out.println("待签名字符串为： "+ sourceStr);

        //用私钥加签
        String signature = RSAUtil.sign(sourceStr, Keys.PRIVATE_KEY_1024_PKSC8);
        System.out.println("得到的签名为： "+ signature);

        //设置签名
        reqDto.setSignature(signature);
        System.out.println("加签后报文为： " + gson.toJson(reqDto));


        //验签
        String resSignature = reqDto.getSignature();//响应报文签名字段
        String resSourceStr = DigestUtil.buildSourceStr("POST",url,reqDto,"signature");//待验证字符串

        boolean isVerify = RSAUtil.verify(resSourceStr,Keys.PUBLIC_KEY_1024_PKCS8,resSignature);//验签
        System.out.println("验签结果： "+isVerify);

        //发送Http请求
//        HttpUriRequest httpPost = RequestBuilder.post().setUri("https://api.jsjngf.com/"+url).build();
//
//        CloseableHttpClient client = HttpClients.createDefault();
//
//        try{
//            String resMsg = client.execute(httpPost, StringResponseHandle.INSTANCE);
//
//            JNResHeaderDTO resDto = gson.fromJson(resMsg,JNResHeaderDTO.class);
//
//            String resSignature = resDto.getSignature();//响应报文签名字段
//            String resSourceStr = DigestUtil.digest("POST",url,resDto,"signature");//待验证字符串
//
//            boolean isVerify = RSAUtil.verify(resSourceStr,Keys.PUBLIC_KEY_1024_PKCS8,resSignature);//验签
//
//            System.out.println("返回的报文为： "+ gson.toJson(resDto));
//        }catch (IOException e) {
//            e.printStackTrace();
//        }finally {
//            try {
//                client.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }

    }


    private static JNReqHeaderDTO buildJNReqHeader(String reqData) {
        JNReqHeaderDTO dto = new JNReqHeaderDTO();
        dto.setApi_key("jnkey001");//金农分配
        dto.setCall_dt(String.valueOf(System.currentTimeMillis()));
        dto.setFormat("json");
        dto.setIe("UTF-8");
        dto.setReq_body(reqData);

        return dto;
    }

    private static ReqBaseDTO buildReqBase(String notifyContent) {
        ReqBaseDTO dto = new ReqBaseDTO();

        dto.setMerchantId("100000178");
        dto.setSubMerchantId("10086");
        dto.setJsSeq("100000000000001");
        dto.setTimestamp(DateFormatUtils.format(new Date(), "yyyyMMddHHmmssSSS"));
        dto.setTransCode("js002001");
        dto.setNotifyContent(notifyContent);
        dto.setDigSign("");//此时可以置为空
        dto.setVersion("1.0.0");
        dto.setReserved("");

        return dto;
    }

    private static ReconciliationFileReadyDTO buildReconciliationFileReady() {
        ReconciliationFileReadyDTO dto = new ReconciliationFileReadyDTO();

        dto.setFileName("testFile001");
        dto.setTransDate(DateFormatUtils.format(new Date(), "yyyyMMdd"));
        dto.setReserved("");

        return dto;
    }

}
