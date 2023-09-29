package com.sgurin.inetback.config;

import com.sgurin.inetback.utils.Utils;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

public class CustomHeaderLocaleResolver extends AcceptHeaderLocaleResolver {

    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        Locale defaultLocale = getDefaultLocale();
        if (defaultLocale != null && request.getHeader("lang") == null) {
            return defaultLocale;
        }

        return Utils.getLanguage(request).getLocale();
    }
}