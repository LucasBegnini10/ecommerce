package com.server.ecommerce.product.utils;

public class PriceUtils {

    public static double formatLongToDouble(long price){
        return (double) price / 100.0;
    }
}
