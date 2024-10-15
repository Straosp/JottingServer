package cn.straosp.jotting.server.module

data class DruidConfig(
    val  url:String,
    val user:String,
    val driver:String,
    val password:String?,
    val defaultConnectSize: Int,
    val maxConnectSize: Int,
    val maxWaitTime: Int

)

interface DruidConfigRepository {
    fun getDruidConfig():DruidConfig
}

class DruidConfigRepositoryImpl : DruidConfigRepository {
    override fun getDruidConfig(): DruidConfig {
        return DruidConfig(
            url = "jdbc:mysql://127.0.0.1:3306/jotting",
            driver = "com.mysql.cj.jdbc.Driver",
            user = "root",
            password = "12345678",
            defaultConnectSize = 10,
            maxConnectSize = 20,
            maxWaitTime = 1000,
        )
    }
}