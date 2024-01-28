package com.server.ecommerce.product.utils;

public class PriceUtils {

    public static double formatLongToDecimal(long price){
        return (double) price / 100.0;
    }
}
