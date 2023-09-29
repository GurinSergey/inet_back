package com.sgurin.inetback.service;

import com.sgurin.inetback.enums.Language;
import com.sgurin.inetback.enums.SystemType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class LocaleMessageService {
    private final MessageSource messageSource;

    @Autowired
    public LocaleMessageService(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String localizedMessage(String code) {
        Locale locale = LocaleContextHolder.getLocale();
        return localizedMessage(code, locale);
    }

    public String localizedMessage(String code, Locale locale) {
        return localizedMessage(code, locale, SystemType.NONE);
    }

    public String localizedMessage(String code, Language language) {
        return localizedMessage(code, language.getLocale(), SystemType.NONE);
    }

    public String localizedMessage(String code, Language language, SystemType systemType) {

        return localizedMessage(code, language.getLocale(), systemType);
    }

    public String localizedMessage(String code, Locale locale, SystemType systemType, Object... args) {
        String systemCode = code;
        try {
            if (systemType != SystemType.NONE) {
                systemCode = systemType.getCode().concat(".").concat(code);
            }

            return messageSource.getMessage(systemCode, args, locale);
        } catch (NoSuchMessageException ex) {
            return messageSource.getMessage(code, args, Locale.CANADA);
        }
    }
}
