package com.example.shopx.Utils;

public class Utils {

    public static String formatPrice(String price) {
        StringBuilder builder = new StringBuilder();
        int cnt = 0;
        for (int i = price.length() - 1; i >= 0; i--) {
            builder.append(price.charAt(i));
            if (i == 0) {
                break;
            }
            cnt++;
            if (cnt == 3) {
                cnt = 0;
                builder.append(',');
            }
        }
        return builder.reverse().toString();
    }

    public static String formatDescription(String description) {
        int lastUppercase = -1, cnt = 0;
        StringBuilder builder = new StringBuilder("");
        for (int i = 0; i < description.length(); i++) {
            char c = description.charAt(i);
            builder.append(c);
            if (c == ':') {
                if (lastUppercase != -1) {
                    builder.insert(lastUppercase + cnt, '\n');
                    cnt++;
                    lastUppercase = -1;
                }
            }
            if (checkUppercase(c)) {
                if (c == 'R' && i + 2 < description.length() && description.charAt(i + 1) == 'A' && description.charAt(i + 2) == 'M') {
                    builder.append("AM");
                    lastUppercase = i;
                    i += 2;
                } else if (c == 'R' && i + 2 < description.length() && description.charAt(i + 1) == 'O' && description.charAt(i + 2) == 'M') {
                    builder.append("OM");
                    lastUppercase = i;
                    i += 2;
                } else if (c == 'C' && i + 2 < description.length() && description.charAt(i + 1) == 'P' && description.charAt(i + 2) == 'U') {
                    builder.append("PU");
                    lastUppercase = i;
                    i += 2;
                } else if (c == 'U' && i + 2 < description.length() && description.charAt(i + 1) == 'S' && description.charAt(i + 2) == 'B') {
                    builder.append("SB");
                    lastUppercase = i;
                    i += 2;
                } else if (c == 'G' && i + 2 < description.length() && description.charAt(i + 1) == 'P' && description.charAt(i + 2) == 'U') {
                    builder.append("PU");
                    lastUppercase = i;
                    i += 2;
                } else if (c == 'S' && i + 2 < description.length() && description.charAt(i + 1) == 'I' && description.charAt(i + 2) == 'M') {
                    builder.append("IM");
                    lastUppercase = i;
                    i += 2;
                } else {
                    lastUppercase = i;
                }
            }
        }
        return builder.toString();
    }

    private static boolean checkUppercase(char c) {
        return c >= 65 && c <= 90;
    }
}
