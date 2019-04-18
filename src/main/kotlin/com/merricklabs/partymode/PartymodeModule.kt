package com.merricklabs.partymode

import com.merricklabs.partymode.storage.PartymodeStorage
import org.koin.dsl.module.module

val PartymodeModule = module {
    single { PartymodeConfig() }
    single { PartymodeStorage() }
}