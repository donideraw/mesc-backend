package com.pertamina.backend.utils;

import com.pertamina.backend.configuration.exception.GeneralException;

public class Checks {
    public static void mustTrue(boolean param, String message) {
        if (!param) throw newE(message);
    }
    public static void mustTrue(boolean param, String message, int status) {
        if (!param) throw newE(message, status);
    }

    public static <U extends RuntimeException> void mustTrue(boolean param, U exception) {
        if (!param) throw exception;
    }

    public static void throwE(String message) {
        throw newE(message);
    }

    public static GeneralException newE(String message) {
        return new GeneralException(message);
    }

    public static GeneralException newE(String message, int status) {
        return new GeneralException(message, status);
    }

    public static GeneralException newE(String message, int status, Object data) {
        return new GeneralException(message, status, data);
    }
}
