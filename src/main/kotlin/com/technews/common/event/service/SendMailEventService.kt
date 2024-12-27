package com.technews.common.event.service

import com.technews.common.event.dto.SendMailEvent
import jakarta.mail.MessagingException
import jakarta.mail.internet.InternetAddress
import jakarta.mail.internet.MimeMessage
import jakarta.mail.internet.MimeUtility
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.MailSendException
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service

private val logger = KotlinLogging.logger {}

@Service
class SendMailEventService(
    private val mailSender: JavaMailSender,
    @Value("\${spring.mail.username}") private val senderAddress: String,
    @Value("\${admin.mail.address}") private val adminAddress: String,
) {
    companion object {
        private const val MAIL_TITLE_PREFIX = "[Tech-News] "
    }

    fun sendMail(request: SendMailEvent) {
        kotlin.runCatching {
            val message: MimeMessage = getMimeMessage(request)
            mailSender.send(message)
        }.onFailure { e ->
            logger.error("[MailUtilService.sendMail] Fail to send mail.", e)
            throw MailSendException("Fail to send mail.")
        }
    }

    private fun getMimeMessage(request: SendMailEvent): MimeMessage {
        return try {
            mailSender.createMimeMessage().apply {
                subject = "$MAIL_TITLE_PREFIX${request.subject}"
                setMailRecipient(this, request.addressList)
                setFrom(InternetAddress(senderAddress, MimeUtility.encodeText("tech-news", "UTF-8", "B")))
                setContent(request.contents, "text/html;charset=UTF-8")
            }
        } catch (e: Exception) {
            logger.error("[SendMailEventService.createMimeMessage] Failed to create mail message.", e)
            throw MailSendException("Failed to create mail message.")
        }
    }

    @Throws(MessagingException::class)
    private fun setMailRecipient(message: MimeMessage, addressList: List<String>) {
        val recipientAddresses = if (addressList.isEmpty()) {
            adminAddress
        } else {
            addressList.joinToString(",")
        }

        message.setRecipients(
            jakarta.mail.Message.RecipientType.TO,
            InternetAddress.parse(recipientAddresses),
        )
    }
}
