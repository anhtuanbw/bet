// Copyright (c) 2015 KMS Technology, Inc.
package vn.kms.ngaythobet.domain.core;

import java.util.Locale;
import java.util.concurrent.Future;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;

import vn.kms.ngaythobet.domain.tournament.Group;

@Service
public class MailService {
    private static final Logger logger = LoggerFactory.getLogger(MailService.class);

    private final JavaMailSender mailSender;

    private final MessageSource messageSource;

    private final SpringTemplateEngine templateEngine;

    @Value("${ngaythobet.mail-sender}")
    private String from;

    @Value("${ngaythobet.webapp-baseurl}")
    private String baseUrl;

    @Autowired
    public MailService(JavaMailSender mailSender, MessageSource messageSource, SpringTemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.messageSource = messageSource;
        this.templateEngine = templateEngine;
    }

    @Async
    public Future<Boolean> sendEmailAsync(String to, String subject, String content, boolean isMultipart, boolean isHtml) {
        logger.debug("Send e-mail[multipart '{}' and html '{}'] to '{}' with subject '{}' and content={}",
            isMultipart, isHtml, to, subject, content);

        // Prepare message using a Spring helper
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        boolean result;
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, "utf-8");
            message.setFrom(from);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(content, isHtml);
            mailSender.send(mimeMessage);
            result = true;
            logger.debug("Sent e-mail to User '{}'", to);
        } catch (Exception ex) {
            result = false;
            logger.error("E-mail could not be sent to user " + to, ex);
        }

        return new AsyncResult<>(result);
    }

    @Async
    public Future<Boolean> sendActivationEmailAsync(User user) {
        logger.debug("Sending activation e-mail to '{}'", user.getEmail());

        Locale locale = Locale.forLanguageTag(user.getLanguageTag());
        Context context = new Context(locale);
        context.setVariable("user", user);
        context.setVariable("baseUrl", baseUrl);
        String content = templateEngine.process("activation", context);
        String subject = messageSource.getMessage("email.activation.title", null, locale);

        return sendEmailAsync(user.getEmail(), subject, content, false, true);
    }

    @Async
    public Future<Boolean> sendPasswordResetMailAsync(User user) {
        logger.debug("Sending password reset e-mail to '{}'", user.getEmail());

        Locale locale = Locale.forLanguageTag(user.getLanguageTag());
        Context context = new Context(locale);
        context.setVariable("user", user);
        context.setVariable("baseUrl", baseUrl);
        String content = templateEngine.process("password-reset", context);
        String subject = messageSource.getMessage("email.reset.title", null, locale);

        return sendEmailAsync(user.getEmail(), subject, content, false, true);
    }
    
    @Async
    public Future<Boolean> sendMailToGroupModeratorAsync(User user, Group group) {
        Locale locale = Locale.forLanguageTag(user.getLanguageTag());
        Context context = new Context(locale);
        context.setVariable("user", user);
        context.setVariable("group", group);
        context.setVariable("baseUrl", baseUrl);
        String content = templateEngine.process("notify-group-moderator", context);
        String subject = messageSource.getMessage("email.notify.group.moderator.title", null, locale);

        return sendEmailAsync(user.getEmail(), subject, content, false, true);
    }
}
