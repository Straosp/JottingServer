package cn.straosp.jotting.server.util

import kotlinx.serialization.Serializable

@Serializable
data class OperationMessage(val code:Int,val errorMsg:String):Throwable()

val ACTION_FAILED = OperationMessage(Constant.ACTION_FAILED_CODE, Constant.ACTION_FAILED_MESSAGE)