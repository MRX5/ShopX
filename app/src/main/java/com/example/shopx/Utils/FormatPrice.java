package com.example.shopx.Utils;

import java.math.BigDecimal;

public class FormatPrice {
    public static String format(String price)
    {
        StringBuilder builder = new StringBuilder();
        int cnt=0;
        for (int i = price.length() - 1; i >= 0; i--) {
            builder.append(price.charAt(i));
            if(i==0){break;}
            cnt++;
            if (cnt == 3) {
                cnt = 0;
                builder.append(',');
            }
        }
        return builder.reverse().toString();
    }
}
