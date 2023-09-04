package com.arsiu.eduhub.service.util.mail

data class EmailParameters(
    val receiver: String,
    val msg: String,
    val subject: String
)

