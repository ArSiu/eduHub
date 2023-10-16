package com.arsiu.eduhub.common.infrastructure.nontification.mail

data class EmailParameters(
    val receiver: String,
    val msg: String,
    val subject: String
)

