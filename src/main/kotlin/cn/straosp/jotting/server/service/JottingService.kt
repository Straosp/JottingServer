package cn.straosp.jotting.server.service

import cn.straosp.jotting.server.db.AddJotting
import cn.straosp.jotting.server.db.JottingReminder
import java.time.LocalDate

interface JottingService {

    fun selectAllJottingByUser(accountID: Int): Result<List<JottingReminder>>
    fun getJottingByDate(accountID: Int,localDate: LocalDate): Result<List<JottingReminder>>
    fun removeOneJotting(accountID: Int,jottingID: Int) : Result<Boolean>
    fun cancelReminder(accountID: Int,jottingID: Int): Result<Boolean>
    fun addOneJotting(accountID: Int,addJotting: AddJotting): Result<Boolean>

}