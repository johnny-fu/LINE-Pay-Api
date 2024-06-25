package com.linepay.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.HmacUtils;
import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.UUID;

@Slf4j
@Service
public class LinePayService {
    @Value("${Linepay.channelId}")
    private String channelId;
    @Value("${Linepay.channelSecret}")
    private String channelSecret;
    @Value("${Linepay.url.base}")
    private String base;
    @Value("${Linepay.url.request}")
    private String request;
    @Value("${Linepay.url.confirm}")
    private String confirm;
    @Value("${Linepay.url.refund}")
    private String refund;
    @Value("${Linepay.url.checkPaymentStatus}")
    private String checkPaymentStatus;
    @Autowired
    private RestTemplate restTemplate;

    public String requestApi(JSONObject sendobject) {
        JSONObject returndata = sendPost(sendobject, request);
        if (returndata == null) {
            return "Error";
        } else {
            return returndata.toString();
        }
    }

    public String ConfirmApi(String transactionId, BigDecimal amount) {
        JSONObject sendobject = new JSONObject();

        sendobject.put("amount", amount);
        sendobject.put("currency", "TWD");
        JSONObject returndata = sendPost(sendobject, confirm.replace("{transactionId}", transactionId));
        if (returndata == null) {
            return "Error";
        } else {
            String returnCode = returndata.getString("returnCode");
            if (returnCode.equals("0000")) {
                return "success";
            }else {
                return "Error";
            }
        }
    }

    public String CheckPaymentStatusAPI(String transactionId) {
        JSONObject returndata = sendGet(checkPaymentStatus.replace("{transactionId}", transactionId));
        if (returndata == null) {
            return "Error";
        } else {
            return returndata.getString("returnCode");
        }
    }

    public String refundApi(String transactionId, BigDecimal amount) {
        JSONObject sendobject = new JSONObject();

        sendobject.put("amount", amount);
        JSONObject returndata = sendPost(sendobject, refund.replace("{transactionId}", transactionId));
        if (returndata == null) {
            return "Error";
        } else {
            return returndata.getString("returnCode");
        }
    }

    public JSONObject sendPost(JSONObject sendobject, String api) {
        try {
            String nonce = UUID.randomUUID().toString();
            // set headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("X-LINE-ChannelId", channelId);
            headers.set("X-LINE-Authorization-Nonce", nonce);
            //JSONObject to String
            headers.set("X-LINE-Authorization", encrypt(channelSecret, channelSecret + api + sendobject.toString() + nonce));
            HttpEntity<String> entity = new HttpEntity<String>(sendobject.toString(), headers);
            // send request and parse result
            ResponseEntity<String> response = restTemplate.exchange(base + api, HttpMethod.POST, entity, String.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                return new JSONObject(response.getBody());
            } else {
                log.error(" api : " + api);
                log.error(" statusCode : " + response.getStatusCode());
                log.error("body : "+response.getBody());
            }
        } catch (Exception e) {
            log.error(e.toString());
        }
        return null;
    }

    public JSONObject sendGet(String api) {
        try {
            HttpEntity<String> entity = new HttpEntity<String>(null, null);
            // send request and parse result
            ResponseEntity<String> response = restTemplate.exchange(base + api, HttpMethod.GET, entity, String.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                return new JSONObject(response.getBody());
            } else {
                log.error(" api : " + api);
                log.error(" statusCode : " + response.getStatusCode());
                log.error("body : "+response.getBody());
            }
        } catch (Exception e) {
            log.error(e.toString());
        }
        return null;
    }

    public static String encrypt(final String keys, final String data) throws UnsupportedEncodingException {
        //如data內有中文 需使用 UTF-8編碼
        return toBase64String(HmacUtils.getHmacSha256(keys.getBytes("UTF-8")).doFinal(data.getBytes("UTF-8")));
//        return toBase64String(HmacUtils.getHmacSha256(keys.getBytes()).doFinal(data.getBytes()));
    }
    public static String toBase64String(byte[] bytes) {
        byte[] byteArray = Base64.encodeBase64(bytes);
        return new String(byteArray);
    }
}

