package cn.straosp.jotting.server.service

import cn.straosp.jotting.server.db.Account

interface AccountService{

    fun login(phone:String,password: String):Result<Account>
    fun register(phone: String,password: String): Result<Account>
    fun userAuth(phone: String,password: String): Result<Account>
    fun saveUserHeader(accountId: Int, fileName: String): Result<Boolean>

}