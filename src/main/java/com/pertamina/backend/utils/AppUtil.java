package com.pertamina.backend.utils;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class AppUtil {
    private static byte[] alphaNumericBytes = "0123456789ABCDEFGHIJKLMNOPQRSQUVWXYZ".getBytes();

    /**
     * generate random String [A-Z, 0-9]
     *
     * @return String generated
     */
    public static String generateRandomTrxCode(String prefix, int length) {
        char spliter = '.';
        if (length < 1) {
            throw new RuntimeException("length cannot be less than 1");
        }
        LocalDateTime now = LocalDateTime.now();
        int alphaNumLength = alphaNumericBytes.length;
        String yearStr = now.format(DateTimeFormatter.ofPattern("yyyy"));
        int day = alphaNumericBytes[now.getDayOfMonth()];
        int month = alphaNumericBytes[now.getMonthValue() % alphaNumLength];
        int year1 = alphaNumericBytes[Integer.parseInt(yearStr.substring(0, 2)) % alphaNumLength];
        int year2 = alphaNumericBytes[Integer.parseInt(yearStr.substring(2, 4)) % alphaNumLength];

        Random random = new Random();
        byte[] defaultPrefixes = new byte[]{(byte) year1, (byte) year2, (byte) month, (byte) day};
        byte[] result = new byte[length];
        for (int i = 0; i < length; i++) {
            result[i] = alphaNumericBytes[random.nextInt(alphaNumLength)];
        }
        return (prefix != null ? prefix : "")
                + new String(defaultPrefixes, StandardCharsets.UTF_8) + spliter
                + new String(result, StandardCharsets.UTF_8);
    }

    public static String getRequestUrlAndMethod(HttpServletRequest request) {
        return request.getMethod() + " " + request.getServletPath() + (request.getQueryString() == null ? "" : "?" + request.getQueryString());
    }
}

