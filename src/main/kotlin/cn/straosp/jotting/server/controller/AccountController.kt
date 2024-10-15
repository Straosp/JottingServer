package cn.straosp.jotting.server.controller

import cn.straosp.jotting.server.plugins.JTWToken
import cn.straosp.jotting.server.service.AccountService
import cn.straosp.jotting.server.util.OperationMessage
import cn.straosp.jotting.server.util.RequestResult
import cn.straosp.jotting.server.util.SaveFileUtil
import io.ktor.http.content.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.jvm.javaio.copyTo
import kotlinx.serialization.Serializable
import org.koin.ktor.ext.inject
import java.io.File

@Serializable
data class Login(val phone: String,val password: String)

@Serializable
data class Token(val token: String)

fun Routing.accountController(){
    val accountService by application.inject<AccountService>()
    route("/account"){
        post("/login"){
            val loginUser = kotlin.runCatching { call.receive<Login>() }.getOrNull() ?: Login(phone = "", password = "")
            if (loginUser.phone.isEmpty() || loginUser.password.isEmpty()){
                call.respond(RequestResult.parameterError())
            }else{
                val operationResult = accountService.login(loginUser.phone,loginUser.password)
                if (operationResult.isFailure){
                    val operationMessage = operationResult.exceptionOrNull() as? OperationMessage
                    call.respond(RequestResult.error(operationMessage?.errorMsg ?: "数据异常，请稍后重试"))
                }else{
                    val account = operationResult.getOrNull()
                    call.respond(RequestResult.success(Token(token = JTWToken.createToken(account?.phone ?: "",account?.password ?: "", account?.id ?: 0))))
                }
            }
        }
        post("/register"){
            try {
                val loginUser = kotlin.runCatching { call.receive<Login>() }.getOrNull() ?: Login(phone = "", password = "")
                if (loginUser.phone.isEmpty() || loginUser.password.isEmpty()){
                    call.respond(RequestResult.parameterError())
                }else{
                    val registerResult = accountService.register(loginUser.phone,loginUser.password)
                    if (registerResult.isSuccess){
                        val account = registerResult.getOrNull()
                        call.respond(RequestResult.success(Token(token = JTWToken.createToken(account?.phone ?: "",account?.password ?: "", account?.id ?: 0))))
                    }else{
                        val operationMessage = registerResult.exceptionOrNull() as? OperationMessage
                        call.respond(RequestResult.error(operationMessage?.errorMsg ?: "数据异常，请稍后重试"))
                    }


                }
            }catch (e: Exception){
                println(e.message)
            }

        }
        authenticate{
            post("/uploadHeader"){
                val jwtPrincipal = call.principal<JWTPrincipal>()
                val accountID = jwtPrincipal?.payload?.getClaim("accountID")?.asInt() ?: 0
                val multiplatform = runCatching { call.receiveMultipart() }.getOrNull()
                if (null == multiplatform){
                    call.respond(RequestResult.error("未收到图片"))
                    return@post
                }
                multiplatform.forEachPart { data ->
                    when (data) {
                        is PartData.FormItem -> {
                            println(data.value)
                        }
                        is PartData.FileItem -> {
                            data.originalFileName?.let {fileName ->
                                val ext = File(fileName).extension
                                val saveFileName = "header_${System.currentTimeMillis()}_${accountID}.$ext"
                                SaveFileUtil.getInstance().saveHeaderFile(saveFileName,data.provider)
                                val result = accountService.saveUserHeader(accountID,saveFileName)
                                if (result.isSuccess){
                                    call.respond(RequestResult.success())
                                }else{
                                    val operationMessage = result.exceptionOrNull() as? OperationMessage
                                    call.respond(RequestResult.error(operationMessage?.errorMsg ?: "数据异常，请稍后重试"))
                                }
                            }
                        }
                        else -> {
                            call.respond(RequestResult.error("不支持的格式"))
                        }
                    }
                    data.dispose()
                }
                call.respond(RequestResult.error())
            }
        }


    }
}