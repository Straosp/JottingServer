package cn.straosp.jotting.server.util

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun String.toLocalDateTime(): LocalDateTime = if (this.isEmpty()) LocalDateTime.now() else LocalDateTime.parse(this,DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
fun LocalDateTime.toFormatterString(formatter: String = "yyyy-MM-dd HH:mm:ss"): String  = this.format(DateTimeFormatter.ofPattern(formatter))

val date = LocalDateTime.now()
fun getCurrentYear(): Int{
    return date.year
}
fun getCurrentMonth(): Int{
    return date.monthValue
}
fun LocalDate.toTimestamp():Long {
    val localDateTime = this.atStartOfDay()
    return localDateTime.toEpochSecond(ZoneId.systemDefault().rules.getOffset(localDateTime)) * 1000L
}
fun LocalDateTime.toTimestamp():Long {
    return this.toEpochSecond(ZoneId.systemDefault().rules.getOffset(this)) * 1000L
}