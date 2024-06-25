package com.linepay.entity.line;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class Product {

    private String id;

    private String name;

    private String imageUrl;

    private Integer quantity;

    private Integer price;

    private Integer originalPrice;

}
