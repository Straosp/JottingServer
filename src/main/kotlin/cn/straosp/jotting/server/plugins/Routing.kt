package cn.straosp.jotting.server.plugins

import cn.straosp.jotting.server.controller.accountController
import cn.straosp.jotting.server.controller.configureAdministration
import cn.straosp.jotting.server.controller.jottingController
import cn.straosp.jotting.server.module.configureKoin
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.resources.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    configureKoin()
    configureHTTP()
    configureSecurity()
    configureAdministration()
    install(Resources)
    routing {
        accountController()
        jottingController()
        staticResources("/static", "static")
    }
}