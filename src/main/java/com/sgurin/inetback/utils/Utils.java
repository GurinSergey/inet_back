package com.sgurin.inetback.utils;

import com.sgurin.inetback.enums.Language;

import javax.servlet.http.HttpServletRequest;

public class Utils {
    public static Language getLanguage(HttpServletRequest request) {
        Language language = Language.EN;
        String userLocale = request.getHeader("lang");
        if (userLocale != null) {
            language = userLocale.equals(Language.UK.getCode()) ? Language.UK : language;
        }

        return language;
    }
}
