package cn.straosp.jotting.server.service

import cn.straosp.jotting.server.db.AddJotting
import cn.straosp.jotting.server.db.JottingReminder

interface JottingService {

    fun selectAllJottingByUser(accountID: Int): Result<List<JottingReminder>>
    fun removeOneJotting(accountID: Int,jottingID: Int) : Result<Boolean>
    fun cancelReminder(accountID: Int,jottingID: Int): Result<Boolean>
    fun addOneJotting(accountID: Int,addJotting: AddJotting): Result<Boolean>

}