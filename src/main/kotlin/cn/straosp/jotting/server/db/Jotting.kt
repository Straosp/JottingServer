package cn.straosp.jotting.server.db

import kotlinx.serialization.Serializable
import org.ktorm.entity.Entity
import org.ktorm.schema.*
import java.time.LocalDateTime

@Serializable
data class Jotting(
    val id: Int,
    val accountID: Int,
    val content: String,
    val tips: String?,
    val isNeedReminder: Boolean,
    val reminderID: Int?,
    val recordDate: String
)

interface JottingEntity : Entity<JottingEntity> {
    companion object : Entity.Factory<JottingEntity>()
    val id: Int
    val accountID: Int
    val content: String
    val tips: String?
    val isNeedReminder: Boolean
    val reminderID: Int?
    val recordDate: LocalDateTime
}

object JottingTable : Table<JottingEntity>("jotting") {
    val id = int("id").primaryKey().bindTo { it.id }
    val accountID = int("account_id").bindTo { it.accountID }
    val content = varchar("content").bindTo { it.content }
    val tips = varchar("tips").bindTo { it.tips  }
    val isNeedReminder = boolean("is_need_reminder").bindTo { it.isNeedReminder }
    val reminderID = int("reminder_id").bindTo { it.reminderID }
    val recordDate = datetime("record_date").bindTo { it.recordDate  }
}


@Serializable
data class AddJotting(
    val content: String,
    val tips: String? = null,
    val isNeedReminder: Boolean,
    val recordDate: String,
    val reminderType: Int? = null, // 0 每秒 1 秒分钟 2 每时， 3每天 4 每星期 5每月 6每年
    val startDate: String? = null,
    val endDate: String? = null,
    val interval: Int = 1,
    val count: Int? = null
)

@Serializable
data class JottingReminder(
    val id: Int,
    val content: String,
    val tips: String?,
    val isNeedReminder: Boolean,
    val recordDate: String,
    val reminder: Reminder? = null
)


