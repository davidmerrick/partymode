package com.merricklabs.partymode

import com.merricklabs.partymode.handlers.CallHandlerLogic
import com.merricklabs.partymode.handlers.SmsHandlerLogic
import com.merricklabs.partymode.slack.SlackNotifier
import com.merricklabs.partymode.storage.PartymodeStorage
import org.koin.dsl.module.module

val PartymodeModule = module {
    single { PartymodeConfig() }
    single { PartymodeStorage() }
    single { SmsHandlerLogic() }
    single { CallHandlerLogic() }
    single { SlackNotifier() }
}