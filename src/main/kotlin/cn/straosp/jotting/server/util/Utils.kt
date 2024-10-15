package cn.straosp.jotting.server.util

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun String.toLocalDateTime(): LocalDateTime = if (this.isEmpty()) LocalDateTime.now() else LocalDateTime.parse(this,DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
fun LocalDateTime.toFormatterString(formatter: String = "yyyy-MM-dd HH:mm:ss"): String  = this.format(DateTimeFormatter.ofPattern(formatter))