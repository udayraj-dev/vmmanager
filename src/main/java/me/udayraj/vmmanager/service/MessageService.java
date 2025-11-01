package me.udayraj.vmmanager.service;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    private final MessageSource messageSource;

    public MessageService(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /**
     * Retrieves a message for the given code and arguments, using the current request's locale.
     *
     * @param code The message code (e.g., "error.unexpected").
     * @param args The arguments to format the message with.
     * @return The formatted, internationalized message.
     */
    public String getMessage(String code, Object... args) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(code, args, locale);
    }
}