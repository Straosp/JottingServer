package cn.straosp.jotting.server.controller

import cn.straosp.jotting.server.db.AddJotting
import cn.straosp.jotting.server.service.JottingService
import cn.straosp.jotting.server.util.OperationMessage
import cn.straosp.jotting.server.util.RequestResult
import cn.straosp.jotting.server.util.getCurrentMonth
import cn.straosp.jotting.server.util.getCurrentYear
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import org.koin.ktor.ext.inject
import java.time.LocalDate
import java.time.LocalDateTime

@Serializable
data class CancelReminder(val id: Int)

@Serializable
data class GetJottingByDate(val year: Int? = null,val month: Int? = null,val day: Int)

fun Routing.jottingController() {

    route("/jotting"){
        val jottingService by application.inject<JottingService>()
        authenticate {
            post("/getAllJotting"){
                val jwtPrincipal = call.principal<JWTPrincipal>()
                val result = jottingService.selectAllJottingByUser(jwtPrincipal?.payload?.getClaim("accountID")?.asInt() ?: 0)
                if (result.isSuccess){
                    call.respond(RequestResult.success(result.getOrNull()))
                }else{
                    val operationMessage = result.exceptionOrNull() as? OperationMessage
                    call.respond(RequestResult.error(operationMessage?.errorMsg ?: "数据异常，请稍后重试"))
                }
            }

            post("/getJottingByDate"){
                val jwtPrincipal = call.principal<JWTPrincipal>()
                val date = runCatching { call.receiveNullable<GetJottingByDate>() }.getOrNull()
                if (null == date){
                    call.respond(RequestResult.parameterError())
                    return@post
                }
                val accountID = jwtPrincipal?.payload?.getClaim("accountID")?.asInt() ?: 0
                val localDate = LocalDate.of(
                    date.year ?: getCurrentYear(),date.month ?: getCurrentMonth(), date.day
                )
                val result = jottingService.getJottingByDate(accountID,localDate)
                if (result.isSuccess){
                    call.respond(RequestResult.success(result.getOrNull()))
                }else{
                    val operationMessage = result.exceptionOrNull() as? OperationMessage
                    call.respond(RequestResult.error(operationMessage?.errorMsg ?: "数据异常，请稍后重试"))
                }
            }

            post("/cancelReminder"){
                val jwtPrincipal = call.principal<JWTPrincipal>()
                val accountID = jwtPrincipal?.payload?.getClaim("accountID")?.asInt() ?: 0
                val cancelReminder = runCatching { call.receive<CancelReminder>() }.getOrNull() ?: CancelReminder(0)
                if (cancelReminder.id < 1){
                    call.respond(RequestResult.parameterError())
                }
                val result = jottingService.cancelReminder(accountID,cancelReminder.id)
                if (result.isSuccess){
                    call.respond(RequestResult.success(result.getOrNull()))
                }else{
                    val operationMessage = result.exceptionOrNull() as? OperationMessage
                    call.respond(RequestResult.error(operationMessage?.errorMsg ?: "数据异常，请稍后重试"))
                }
            }
            post("/removeOneJotting"){
                val jwtPrincipal = call.principal<JWTPrincipal>()
                val accountID = jwtPrincipal?.payload?.getClaim("accountID")?.asInt() ?: 0
                val cancelReminder = runCatching { call.receive<CancelReminder>() }.getOrNull() ?: CancelReminder(0)
                val result = jottingService.removeOneJotting(accountID,cancelReminder.id)
                if (result.isSuccess){
                    call.respond(RequestResult.success(result.getOrNull()))
                }else{
                    val operationMessage = result.exceptionOrNull() as? OperationMessage
                    call.respond(RequestResult.error(operationMessage?.errorMsg ?: "数据异常，请稍后重试"))
                }
            }
            post("/addOneJotting"){
                val addJotting = runCatching { call.receiveNullable<AddJotting>() }.getOrNull()
                val jwtPrincipal = call.principal<JWTPrincipal>()
                val accountID = jwtPrincipal?.payload?.getClaim("accountID")?.asInt() ?: 0
                println("AddJotting: $addJotting   $accountID")
                if (null == addJotting){
                    call.respond(RequestResult.parameterError())
                }
                addJotting?.let{
                    val result = jottingService.addOneJotting(accountID,addJotting)
                    if (result.isSuccess){
                        call.respond(RequestResult.success(result.getOrNull()))
                    }else{
                        val operationMessage = result.exceptionOrNull() as? OperationMessage
                        call.respond(RequestResult.error(operationMessage?.errorMsg ?: "数据异常，请稍后重试"))
                    }
                }
            }

        }

    }

}