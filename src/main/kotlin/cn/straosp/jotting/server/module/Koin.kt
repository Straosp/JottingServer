package cn.straosp.jotting.server.module

import cn.straosp.jotting.server.service.AccountService
import cn.straosp.jotting.server.service.JottingService
import cn.straosp.jotting.server.service.impl.AccountServiceImpl
import cn.straosp.jotting.server.service.impl.JottingServiceImpl
import io.ktor.server.application.*
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin

val serviceModule = module {
    single<AccountService> { AccountServiceImpl() }
    single<JottingService> { JottingServiceImpl() }

}
val dataBaseModule = module {
    single<DruidConfigRepository>{ DruidConfigRepositoryImpl() }
}

fun Application.configureKoin(){
    install(Koin) {
        modules(
            serviceModule,
            dataBaseModule
        )

    }
}