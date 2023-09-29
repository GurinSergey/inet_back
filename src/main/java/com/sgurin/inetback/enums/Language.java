package com.sgurin.inetback.enums;

import java.util.Arrays;
import java.util.Locale;

import static java.util.Locale.*;

public enum Language {
    EN("en", "English", ENGLISH),
    UK("uk", "Ukraine", new Locale.Builder().setLanguage("uk").build());

    private String code;
    private String description;
    private Locale locale;

    Language(String code, String description, Locale locale) {
        this.code = code;
        this.description = description;
        this.locale = locale;
    }

    public String getCode() {
        return code;
    }

    public String getIsoCode() {
        return code.replace("_","-");
    }

    public String getDescription() {
        return description;
    }

    public Locale getLocale() {
        return locale;
    }

    public static Language findByLocale(final Locale locale){
        return Arrays.stream(values()).filter(value -> value.getLocale().equals(locale)).findFirst()
                .orElse(Language.EN);
    }
}