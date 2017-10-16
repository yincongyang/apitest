package com.jsjn.apitest.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 恶汉式 单例模式
 * Created by yincongyang on 17/9/28.
 */
public class StringResponseHandle implements ResponseHandler<String>{

    private final static Logger logger = LoggerFactory.getLogger(StringResponseHandle.class);

    //单例模式
    public final static StringResponseHandle INSTANCE = new StringResponseHandle();

    private AtomicInteger httpCount = new AtomicInteger(0);

    private StringResponseHandle(){

    }

    @Override
    public String handleResponse(HttpResponse response) throws IOException {
        int status = response.getStatusLine().getStatusCode();
        if (status >= 200 && status < 300) {
            HttpEntity entity = response.getEntity();
            ContentType contentType = ContentType.getOrDefault(entity);
            Charset charset = contentType.getCharset();
            logger.info("http请求次数：{}",httpCount.incrementAndGet());
            return entity != null ? EntityUtils.toString(entity,charset) : null;
        } else {
            throw new ClientProtocolException("Unexpected response status: " + status);
        }
    }
}
