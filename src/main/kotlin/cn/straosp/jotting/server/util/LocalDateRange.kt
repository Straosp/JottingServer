package cn.straosp.jotting.server.util

import java.time.LocalDateTime

class LocalDateRange(
    override val start: LocalDateTime,
    override val endInclusive: LocalDateTime
) : ClosedRange<LocalDateTime> {

    override fun contains(value: LocalDateTime): Boolean {
        val startLocalDate = start.toTimestamp()
        val endLocalDate = endInclusive.toTimestamp()
        val valueLocalDate = value.toTimestamp()
        return valueLocalDate in startLocalDate..endLocalDate
    }

    override fun isEmpty(): Boolean {
        val startLocalDate = start.toTimestamp()
        val endLocalDate = endInclusive.toTimestamp()
        return startLocalDate > endLocalDate
    }

}