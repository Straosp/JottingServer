package cn.straosp.jotting.server.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date

object JTWToken {

    const val AUDIENCE = "jotting"
    const val ISSUER = "straosp.cn"
    const val SECRET = "straosp"
    const val REALM = "app"

    fun createToken(phone: String,password: String,id: Int): String{
        if (phone.isEmpty() || password.isEmpty() || id < 1) return ""
        val localDateTime = Date.from(LocalDateTime.now(ZoneId.systemDefault()).plusMonths(1).atZone(ZoneId.systemDefault()).toInstant())
        return JWT.create()
            .withAudience(AUDIENCE)
            .withIssuer(ISSUER)
            .withClaim("phone",phone)
            .withClaim("password", password)
            .withClaim("accountID",id)
            .withExpiresAt(localDateTime)
            .sign(Algorithm.HMAC256(SECRET))
    }

}