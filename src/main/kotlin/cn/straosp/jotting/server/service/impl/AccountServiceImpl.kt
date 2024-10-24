package cn.straosp.jotting.server.service.impl

import cn.straosp.jotting.server.db.Account
import cn.straosp.jotting.server.db.AccountTable
import cn.straosp.jotting.server.db.AppDatabase
import cn.straosp.jotting.server.db.toAccount
import cn.straosp.jotting.server.service.AccountService
import cn.straosp.jotting.server.util.Constant
import cn.straosp.jotting.server.util.OperationMessage
import org.ktorm.dsl.and
import org.ktorm.dsl.eq
import org.ktorm.dsl.insertAndGenerateKey
import org.ktorm.dsl.update
import org.ktorm.entity.find
import org.ktorm.entity.sequenceOf

class AccountServiceImpl : AccountService {

    override fun login(phone: String,password: String): Result<Account> {
        val account = AppDatabase.database.sequenceOf(AccountTable).find { (it.phone eq  phone) and (it.password eq it.password)}?.toAccount()
        account?.let {
            return Result.success(account)
        }
        return Result.failure(OperationMessage(Constant.NOT_FOUND_ACCOUNT_CODE, Constant.NOT_FOUND_ACCOUNT_MESSAGE))
    }

    override fun register(phone: String, password: String): Result<Account> {
        AppDatabase.database.useTransaction {
            val account = AppDatabase.database.sequenceOf(AccountTable).find {it.phone eq phone }
            if (null == account){
                val id = AppDatabase.database.insertAndGenerateKey(AccountTable){
                    set(it.phone,phone)
                    set(it.password,password)
                    set(it.username,"起个名字吧")
                }
                val lastUser = AppDatabase.database.sequenceOf(AccountTable).find { it.id eq (id as Int) }
                assert(lastUser != null)
                return Result.success(lastUser?.toAccount()!!)
            }else{
                return Result.failure(OperationMessage(Constant.REGISTER_PHONE_USED_CODE, Constant.REGISTER_PHONE_USED_MESSAGE))
            }

        }
    }

    override fun userAuth(phone: String, password: String): Result<Account> {
        val account = AppDatabase.database.sequenceOf(AccountTable).find { (it.phone eq  phone) and (it.password eq it.password)}?.toAccount()
        account?.let {
            return Result.success(account)
        }
        return Result.failure(OperationMessage(Constant.NOT_FOUND_ACCOUNT_CODE, Constant.NOT_FOUND_ACCOUNT_MESSAGE))
    }

    override fun saveUserHeader(accountId: Int, fileName: String): Result<Boolean> {
        val result = AppDatabase.database.update(AccountTable){
            set(it.header,fileName)
            where {
                it.id eq accountId
            }
        }
        return if (result > 0) Result.success(true) else Result.failure(OperationMessage(Constant.UPDATE_ACCOUNT_HEADER_FAILED_CODE,
            Constant.UPDATE_ACCOUNT_HEADER_FAILED_MESSAGE))
    }
}