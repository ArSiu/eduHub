package com.arsiu.eduhub.service.util.mail

interface EmailService {
    fun sendMail(details: EmailParameters)
}
