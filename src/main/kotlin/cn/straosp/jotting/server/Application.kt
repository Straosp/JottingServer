package cn.straosp.jotting.server

import cn.straosp.jotting.server.plugins.configureRouting
import cn.straosp.jotting.server.util.SaveFileUtil
import io.ktor.server.application.*
import io.ktor.server.netty.*

fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun Application.module() {
    SaveFileUtil.initEnvironment(environment)
    configureRouting()
}
