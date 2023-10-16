package com.arsiu.eduhub.common.infrastructure.nontification.mail

interface EmailService {

    fun sendMail(details: EmailParameters)

}
