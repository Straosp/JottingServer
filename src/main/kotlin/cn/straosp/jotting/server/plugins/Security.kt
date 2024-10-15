package cn.straosp.jotting.server.plugins

import cn.straosp.jotting.server.service.AccountService
import cn.straosp.jotting.server.util.Constant
import cn.straosp.jotting.server.util.RequestResult
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.respond
import org.koin.ktor.ext.inject

fun Application.configureSecurity() {
    val accountService by inject<AccountService>()
    authentication {
        jwt {
            realm = JTWToken.REALM
            verifier(
                JWT
                    .require(Algorithm.HMAC256(JTWToken.SECRET))
                    .withAudience(JTWToken.AUDIENCE)
                    .withIssuer(JTWToken.ISSUER)
                    .build()
            )
            validate { credential ->
                val phone = credential.payload.getClaim("phone").asString()
                val password = credential.payload.getClaim("password").asString()
                if (accountService.userAuth(phone,password).isSuccess){
                    JWTPrincipal(credential.payload)
                }else {
                    null
                }
            }
            challenge { defaultScheme, realm ->
                call.respond(RequestResult.error(Constant.AUTHENTICATION_FAILED_CODE, Constant.AUTHENTICATION_FAILED_MESSAGE))
            }
        }
    }
}
