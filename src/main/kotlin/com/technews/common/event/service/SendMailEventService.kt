package com.technews.common.event.service

import com.technews.aggregate.subscribe.dto.SubscriberMailContents
import com.technews.common.event.dto.SendMailEvent
import jakarta.mail.Message.RecipientType
import jakarta.mail.internet.InternetAddress
import jakarta.mail.internet.MimeMessage
import jakarta.mail.internet.MimeUtility
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.MailSendException
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service
import org.thymeleaf.context.Context
import org.thymeleaf.spring6.SpringTemplateEngine

private val logger = KotlinLogging.logger {}

@Service
class SendMailEventService(
    private val mailSender: JavaMailSender,
    private val templateEngine: SpringTemplateEngine,
    @Value("\${admin.mail.address}") private val adminAddress: String,
    @Value("\${admin.mail.host}") private val host: String,
) {
    companion object {
        private const val MAIL_TITLE_PREFIX = "[Tech-News] "
    }

    fun sendMail(request: SendMailEvent) {
        runCatching {
            mailSender.send(createMimeMessage(request))
        }.onFailure { e ->
            logger.error("[MailUtilService.sendMail] Fail to send mail.", e)
            throw MailSendException("Fail to send mail.")
        }
    }

    private fun createMimeMessage(request: SendMailEvent): MimeMessage {
        return try {
            mailSender.createMimeMessage().apply {
                subject = "$MAIL_TITLE_PREFIX${request.subject}"
                setRecipients(
                    RecipientType.TO,
                    InternetAddress.parse(
                        request.addressList.takeIf { it.isNotEmpty() }
                            ?.joinToString(",")
                            ?: adminAddress,
                    ),
                )
                setFrom(InternetAddress("no-reply@technews.com", MimeUtility.encodeText("tech-news", "UTF-8", "B")))
                setContent(createTemplate(request.contents), "text/html;charset=UTF-8")
            }
        } catch (e: Exception) {
            logger.error("[SendMailEventService.createMimeMessage] Failed to create mail message.", e)
            throw MailSendException("Failed to create mail message.")
        }
    }

    fun createTemplate(contents: SubscriberMailContents): String {
        val context = Context().apply {
            setVariables(mapOf("contents" to contents, "host" to host))
        }
        return templateEngine.process("mail/mail", context)
    }
}
