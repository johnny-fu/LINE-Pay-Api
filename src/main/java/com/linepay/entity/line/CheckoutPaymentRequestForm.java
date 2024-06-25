package com.linepay.entity.line;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import com.linepay.entity.line.Package;
import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class CheckoutPaymentRequestForm {

    private Integer amount;

    private String currency;

    private String orderId;

    private List<Package> packages;

    private RedirectUrl redirectUrls;
}
