package com.jsjn.apitest;

import com.google.gson.Gson;
import com.jsjn.apitest.dto.JNReqHeaderDTO;
import com.jsjn.apitest.dto.JNResHeaderDTO;
import com.jsjn.apitest.dto.ReconciliationFileReadyDTO;
import com.jsjn.apitest.dto.ReqBaseDTO;
import com.jsjn.apitest.util.DigestUtil;
import com.jsjn.apitest.util.RSAUtil;
import com.jsjn.apitest.util.Keys;
import com.jsjn.apitest.util.StringResponseHandle;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Date;

/**
 * 对账文件就绪通知接口示例
 * <p>
 * Created by yincongyang on 17/10/16.
 */
public class ApiTest {

    private static final Logger logger = LoggerFactory.getLogger(ApiTest.class);

    private static final String URI_HOST = "https://api.jsjngf.com";
    private static final String URI_PATH = "/v1/custom/reconciliation_file_ready_notify";

    public static void main(String[] args) throws Exception {
        logger.info("构造请求报文...");

        Gson gson = new Gson();

        //组装业务字段
        ReconciliationFileReadyDTO rfrDto = buildReconciliationFileReady();
        logger.info("业务字段： {}", gson.toJson(rfrDto));

        //加上结算平台通用报文头
        ReqBaseDTO rbDto = buildReqBase(rfrDto);
        logger.info("业务报文： {}", gson.toJson(rbDto));

        //加上金农API平台报文头
        JNReqHeaderDTO reqDto = buildJNReqHeader(rbDto);
        logger.info("未加签报文： {}", gson.toJson(reqDto));

        //获取待签名字符串
        String sourceStr = DigestUtil.buildSourceStr("POST", URI_PATH, reqDto, "signature");
        logger.info("待签名字符串为： {}", sourceStr);

        //结算平台私钥加签
        String signature = RSAUtil.sign(sourceStr, Keys.PRIVATE_KEY_1024_PKSC8);
        logger.info("得到的签名为： {}", signature);

        //设置签名
        reqDto.setSignature(signature);
        logger.info("加签后报文为： {}", gson.toJson(reqDto));


        //发送Http/Https请求
        HttpUriRequest httpPost = RequestBuilder.post().setUri(URI_HOST + URI_PATH).build();
        CloseableHttpClient client = HttpClients.createDefault();

        try {
            String resMsg = client.execute(httpPost, StringResponseHandle.INSTANCE);

            JNResHeaderDTO resDto = gson.fromJson(resMsg, JNResHeaderDTO.class);

            //获取返回报文签名字段
            String resSignature = resDto.getSignature();
            //获取待验证字符串
            String resSourceStr = DigestUtil.buildSourceStr("POST", URI_PATH, resDto, "signature");

            //使用金农公钥验签
            boolean isVerify = RSAUtil.verify(resSourceStr, Keys.PUBLIC_KEY_1024_PKCS8, resSignature);

            logger.info("返回的报文为： {}", gson.toJson(resDto));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private static JNReqHeaderDTO buildJNReqHeader(ReqBaseDTO rbDto) {
        JNReqHeaderDTO dto = new JNReqHeaderDTO();
        dto.setApi_key("jnkey001");//金农分配
        dto.setCall_dt(String.valueOf(System.currentTimeMillis()));
        dto.setFormat("json");
        dto.setIe("UTF-8");
        dto.setReq_body(rbDto);

        return dto;
    }

    private static ReqBaseDTO buildReqBase(ReconciliationFileReadyDTO rfrDto) {
        ReqBaseDTO dto = new ReqBaseDTO();

        dto.setMerchantId("100000178");
        dto.setSubMerchantId("10086");
        dto.setJsSeq("100000000000001");
        dto.setTimestamp(DateFormatUtils.format(new Date(), "yyyyMMddHHmmssSSS"));
        dto.setTransCode("js002001");
        dto.setNotifyContent(rfrDto);
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
