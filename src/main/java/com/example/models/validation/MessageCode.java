package com.example.models.validation;

public final class MessageCode {
    private MessageCode() {
    }

    public static final String LOGIN = "USERNAME";
    public static final String PWD = "PASSWORD";
    public static final String F_NAME = "FIRST_NAME";
    public static final String L_NAME = "LAST_NAME";
    public static final String EMAIL = "EMAIL";
    public static final String ROLE = "ROLE";

    public static final String NULL = "_IS_NULL";
    public static final String BLANK = "_IS_BLANK";
    public static final String TOO_SHORT = "_TOO_SHORT";
    public static final String TOO_LONG = "_TOO_LONG";
}

