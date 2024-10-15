package cn.straosp.jotting.server

import cn.straosp.jotting.server.controller.configureAdministration
import cn.straosp.jotting.server.module.configureKoin
import cn.straosp.jotting.server.plugins.configureHTTP
import cn.straosp.jotting.server.plugins.configureRouting
import cn.straosp.jotting.server.plugins.configureSecurity
import cn.straosp.jotting.server.util.SaveFileUtil
import io.ktor.server.application.*
import io.ktor.server.engine.EmbeddedServer
import io.ktor.server.netty.*

fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun Application.module() {
    SaveFileUtil.initEnvironment(environment)
    configureRouting()
}
