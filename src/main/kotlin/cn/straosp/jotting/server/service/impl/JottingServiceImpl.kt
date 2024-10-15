package cn.straosp.jotting.server.service.impl

import cn.straosp.jotting.server.db.*
import cn.straosp.jotting.server.service.JottingService
import cn.straosp.jotting.server.util.Constant
import cn.straosp.jotting.server.util.OperationMessage
import cn.straosp.jotting.server.util.toFormatterString
import cn.straosp.jotting.server.util.toLocalDateTime
import org.ktorm.dsl.*
import java.time.LocalDateTime

class JottingServiceImpl : JottingService {

    override fun selectAllJottingByUser(accountID: Int): Result<List<JottingReminder>> {
        val result = AppDatabase.database.from(JottingTable)
            .leftJoin(ReminderTable, on = JottingTable.reminderID eq ReminderTable.id)
            .select(
                JottingTable.id,
                JottingTable.content,
                JottingTable.tips,
                JottingTable.isNeedReminder,
                JottingTable.recordDate,
                ReminderTable.id,
                ReminderTable.reminderType,
                ReminderTable.interval,
                ReminderTable.count,
                ReminderTable.startDate,
                ReminderTable.endDate
            )
            .where {
                JottingTable.accountID eq accountID
            }
            .orderBy(JottingTable.recordDate.desc())
            .map { row ->
                JottingReminder(
                    id = row[JottingTable.id] as Int,
                    content = row[JottingTable.content] ?: "",
                    tips = row[JottingTable.tips],
                    isNeedReminder = row[JottingTable.isNeedReminder] as Boolean,
                    recordDate = (row[JottingTable.recordDate] as LocalDateTime).toFormatterString(),
                    reminder = if (row[ReminderTable.id] == null) null else Reminder(
                        id = row[ReminderTable.id] as Int,
                        reminderType = row[ReminderTable.reminderType] as Int,
                        interval = row[ReminderTable.interval] as Int,
                        count = row[ReminderTable.count] as Int,
                        startDate = (row[ReminderTable.startDate] as LocalDateTime).toFormatterString(),
                        endDate = row[ReminderTable.endDate]?.toString()
                    )

                )
            }

        return Result.success(result)
            

    }

    override fun removeOneJotting(accountID: Int,jottingID: Int): Result<Boolean> {
        val result = AppDatabase.database.delete(JottingTable){
            it.accountID eq accountID and (it.id eq jottingID)
        }
        return if (result > 0) Result.success(true) else Result.failure(OperationMessage(1,"test"))

    }

    override fun cancelReminder(accountID: Int,jottingID: Int): Result<Boolean> {
        val result = AppDatabase.database.update(JottingTable){
            set(it.isNeedReminder,false)
            where {
                it.accountID eq accountID and (it.id eq jottingID)
            }
        }
        return if (result > 0) Result.success(true) else Result.failure(OperationMessage(1,"test"))
    }

    override fun addOneJotting(accountID: Int,addJotting: AddJotting): Result<Boolean> {
        AppDatabase.database.useTransaction {transaction ->
            var reminderId = 0
            if (addJotting.isNeedReminder){
                reminderId= AppDatabase.database.insertAndGenerateKey(ReminderTable){
                    set(it.reminderType,addJotting.reminderType)
                    set(it.startDate,addJotting.startDate?.toLocalDateTime())
                    if (!addJotting.endDate.isNullOrEmpty()){
                        set(it.endDate,addJotting.endDate.toLocalDateTime())
                    }
                    set(it.interval,addJotting.interval)
                    if (null != addJotting.count && addJotting.count > 0){
                        set(it.count,addJotting.count)
                    }
                } as Int
            }
            val result = AppDatabase.database.insertAndGenerateKey(JottingTable){
                set(it.content,addJotting.content)
                set(it.tips,addJotting.tips)
                set(it.isNeedReminder,addJotting.isNeedReminder)
                set(it.recordDate,addJotting.recordDate.toLocalDateTime())
                set(it.accountID,accountID)
                set(it.reminderID,reminderId)
            } as Int
            return if (result > 0) Result.success(true) else Result.failure(OperationMessage(Constant.CREATE_JOTTING_FAILED_CODE,Constant.CREATE_JOTTING_FAILED_MESSAGE))
        }
    }


}