package com.technews.common.event.service;

import com.technews.common.event.dto.SendMailEvent;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeUtility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.joining;

@Slf4j
@Service
@RequiredArgsConstructor
public class SendMailEventService {

    @Value("${spring.mail.username}")
    private String senderAddress;

    @Value("${admin.mail.address}")
    private String adminAddress;

    private static final String MAIL_TITLE_PREFIX = "[tech-news] ";

    private final JavaMailSender mailSender;

    public void sendMail(SendMailEvent request) {
        try {
            MimeMessage message = getMimeMessage(request);
            mailSender.send(message);
        } catch (Exception e) {
            log.error("[MailUtilService.sendMail] Fail to send mail.");
            throw new MailSendException("Fail to send mail.");
        }
    }

    private MimeMessage getMimeMessage(SendMailEvent request) {
        String subject = request.getSubject();
        String contents = request.getContents();
        Optional<List<String>> addressList = request.getAddressList();

        MimeMessage message = mailSender.createMimeMessage();
        try {
            message.setSubject(MAIL_TITLE_PREFIX + subject);
            setMailRecipient(message, addressList);
            message.setFrom(new InternetAddress(senderAddress, MimeUtility.encodeText("tech-news", "UTF-8", "B")));
            message.setContent(contents, "text/html;charset=UTF-8");
        } catch (Exception e) {
            log.error("[MailUtilService.getMimeMessage] exception.");
            throw new MailSendException("Fail to generate mail.");
        }

        return message;
    }

    private void setMailRecipient(MimeMessage message, Optional<List<String>> addressList) throws MessagingException {
        if (addressList.isEmpty()) {
            message.setRecipients(
                    Message.RecipientType.TO, 
                    InternetAddress.parse(adminAddress)
            );
            return;
        }

        message.setRecipients(
                Message.RecipientType.TO,
                InternetAddress.parse(addressList.get().stream().collect(joining(",")))
        );
    }
}

