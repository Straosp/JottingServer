package cn.straosp.jotting.server.db

import kotlinx.serialization.Serializable
import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

@Serializable
data class Account(
    val id: Int,
    val username: String,
    val header: String,
    val phone: String,
    val password: String
)

interface AccountEntity : Entity<AccountEntity> {
    companion object : Entity.Factory<AccountEntity>()
    val id: Int
    val username: String
    val header: String
    val phone: String
    val password: String
}

object AccountTable : Table<AccountEntity>("account") {
    val id = int("id").primaryKey().bindTo { it.id }
    val username = varchar("username").bindTo {it.username}
    val header = varchar("header").bindTo {it.header}
    val phone = varchar("phone").bindTo {it.phone}
    val password = varchar("password").bindTo {it.password}
}

fun AccountEntity.toAccount(): Account = Account(id = this.id,username = this.username,header = this.header,phone = this.phone,password = this.password)