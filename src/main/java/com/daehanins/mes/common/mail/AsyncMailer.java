package com.daehanins.mes.common.mail;

import com.daehanins.mes.common.exception.BizException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.activation.DataSource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

@Component
public class AsyncMailer implements Mailer {

    private static final Logger log = LoggerFactory.getLogger(AsyncMailer.class);

    private JavaMailSender mailSender;

    public AsyncMailer(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    @Override
    public void send(Message message) {
        Assert.notNull(message, "Parameter `message` must not be null");

        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(message.getFrom());
            mailMessage.setSubject(message.getSubject());
            mailMessage.setText(message.getBody());
            mailMessage.setTo(message.getTo());
            if (!message.getCc()[0].isEmpty()) {
                mailMessage.setCc(message.getCc());
            }
            mailSender.send(mailMessage);

        } catch (MailException e) {
            log.error("Failed to send mail message", e);
        }
    }

    @Async
    @Override
    public void send(Message message, Map<String, DataSource> files) {
        Assert.notNull(message, "Parameter `message` must not be null");
        Assert.notNull(files, "Parameter `files` must not be null");

        try {
            MimeMessage mailMessage = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(mailMessage, true, "UTF-8");
            messageHelper.setFrom(message.getFrom());
            messageHelper.setSubject(message.getSubject());
            messageHelper.setText(message.getBody(), true);
            messageHelper.setTo(message.getTo());
            if (!message.getCc()[0].isEmpty()) {
                messageHelper.setCc(message.getCc());
            }

            for( Map.Entry<String, DataSource> fileItem : files.entrySet() ){
                messageHelper.addAttachment(fileItem.getKey(), fileItem.getValue());

                FileSystemResource fileSystemResource;
            }

            mailSender.send(mailMessage);

        } catch (MessagingException e) {
            log.error("Failed to send mail message", e);
            throw new BizException("MessageException:" + e.getMessage());
        } catch (MailException e) {
            log.error("Failed to send mail message", e);
            throw new BizException("MailException:" + e.getMessage());
        }
    }

}