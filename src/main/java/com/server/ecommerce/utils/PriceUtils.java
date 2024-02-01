package com.server.ecommerce.utils;

import java.util.HashMap;
import java.util.Map;

public class PriceUtils {

    public static double formatLongToDouble(long price){
        return (double) price / 100.0;
    }

    public static Map<String, Object> buildPriceDetails(long price){
        Map<String, Object> priceDetail = new HashMap<String, Object>();

        double priceDouble = PriceUtils.formatLongToDouble(price);

        priceDetail.put("priceInt", price);
        priceDetail.put("priceDouble", priceDouble);
        priceDetail.put("priceString", MoneyUtils.formatCurrency(priceDouble));
        priceDetail.put("currency", "BRL");
        priceDetail.put("currencySymbol", "R$");

        return priceDetail;
    }
}
