package com.arsiu.eduhub.service.util.mail

import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service

@Service
class EmailServiceImpl(
    private val jms: JavaMailSender,
    @Value("\${spring.mail.username}") private val sender: String
) : EmailService {

    override fun sendMail(details: EmailParameters) {
        val smm = SimpleMailMessage().apply {
            from = sender
            setTo(details.receiver)
            text = details.msg
            subject = details.subject
        }

        jms.send(smm)
    }
}
