package com.linepay.controller;

import com.linepay.entity.line.CheckoutPaymentRequestForm;
import com.linepay.service.LinePayService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/linepay")
@Slf4j
@RestController
public class LinepayController {
    @Autowired
    private LinePayService linePayService;
    @PostMapping(path ="/request", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public String request(HttpServletResponse response, @RequestBody CheckoutPaymentRequestForm checkoutPaymentRequestForm){
        log.info("linepay 請求付款：{}", checkoutPaymentRequestForm);
        String returnData = linePayService.requestApi(new JSONObject(checkoutPaymentRequestForm));

        if (!returnData.contains("Error")) {
            return returnData;
        } else {
            response.setStatus(400);
            return returnData;
        }
    }
    @PostMapping("/confirm")
    public String confirm(HttpServletResponse response, @RequestBody JSONObject data){
        log.info("linepay 確認付款：{}", data);

        String returnData = linePayService.ConfirmApi(data.getString("transactionId"),data.getBigDecimal("amount"));

        if (!returnData.contains("Error")) {
            return returnData;
        } else {
            response.setStatus(400);
            return returnData;
        }
    }
    @GetMapping("/CheckPaymentStatus/{transactionId}")
    public String CheckPaymentStatus(HttpServletResponse response, @PathVariable String transactionId){
        log.info("linepay 確認付款狀態：{}", transactionId);

        String returnData = linePayService.CheckPaymentStatusAPI(transactionId);

        if (!returnData.contains("Error")) {
            return returnData;
        } else {
            response.setStatus(400);
            return returnData;
        }
    }
    @PostMapping("/refund")
    public String refund(HttpServletResponse response, @RequestBody JSONObject data){
        log.info("linepay 退款：{}", data);

        String returnData = linePayService.refundApi(data.getString("transactionId"),data.getBigDecimal("amount"));

        if (!returnData.contains("Error")) {
            return returnData;
        } else {
            response.setStatus(400);
            return returnData;
        }
    }
}
