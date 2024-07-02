package ru.mpei.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import java.io.IOException;

@Slf4j
public class HttpUtils {

    public static HttpPost createHttpPost(String address, String postData) throws IOException {
        var postRequest = new HttpPost(address);
        postRequest.addHeader("Content-Type", "application/json");
        postRequest.addHeader("Connection", "keep-alive");
        postRequest.setEntity(new StringEntity(postData));

        log.info("Post data: {}:   {}", postRequest, EntityUtils.toString(postRequest.getEntity()));

        return postRequest;
    }
}
