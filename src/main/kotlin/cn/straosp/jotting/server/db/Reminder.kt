package cn.straosp.jotting.server.db

import kotlinx.serialization.Serializable
import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.datetime
import org.ktorm.schema.int
import java.time.LocalDateTime


@Serializable
data class Reminder(
    val id: Int,
    val reminderType: Int,
    val interval: Int,
    val count: Int,
    val startDate: String,
    val endDate: String?,
)

interface ReminderEntity : Entity<ReminderEntity> {
    companion object : Entity.Factory<ReminderEntity>()
    val id: Int
    val reminderType: Int
    val interval: Int
    val count: Int
    val startDate: LocalDateTime
    val endDate: LocalDateTime?
}

object ReminderTable : Table<ReminderEntity>("reminder") {
    val id = int("id").primaryKey().bindTo { it.id  }
    val reminderType = int("reminder_type").bindTo { it.reminderType }
    val startDate = datetime("start_date").bindTo { it.startDate }
    val endDate = datetime("end_date").bindTo { it.endDate }
    val interval = int("interval").bindTo { it.interval }
    val count = int("count").bindTo { it.count  }
}
/*


    reminderType: 0 每秒 1 秒分钟 2 每时， 3每天 4 每星期 5每月 6每年
    interval 时间间隔，对应上面的间隔。每秒，每2年，每2天等
    count 事件发生次数，次数达到后截止
    UNTIL 结束时间
    BYMONTH > BYWEEKNO > BYYEARDAY > BYMONTHDAY > BYDAY > BYHOUR > BYMINUTE > BYSECOND
    表示事件发生的具体时间,几月的第几天几点几分几秒等。
    例子： RRULE:FREQ=YEARLY;INTERVAL=2;BYMONTH=1;BYDAY=SU;BYHOUR=8,9;BYMINTUE=30
    每两(INTERVAL=2)年(FREQ=2)的第一(BYMONTH=1)个月(BYMONTH)的所有周日(BYDAY=SU)的8点30分和9点30分(BYHOUR=8,9; BYMINTUE=30)

    id
    reminderType
    count
    startDate
    endDate ?
    advanceMinute 提前提醒按照分钟计算
    endDate == null count = 0 表示无限制循环时间






 */