package com.server.ecommerce.utils;

import java.text.NumberFormat;
import java.util.Locale;

public class MoneyUtils {


    public static String formatCurrency(Object amount){
        Locale locale = new Locale("pt-br", "BR");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
        String formatted = currencyFormatter.format(amount);

        return formatted.replace(".", "#").replace(",", ".").replace("#", ",");
    }
}
