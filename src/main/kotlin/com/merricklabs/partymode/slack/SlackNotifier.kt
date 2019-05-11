package com.merricklabs.partymode.slack

import com.merricklabs.partymode.PartymodeConfig
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

class SlackNotifier : KoinComponent {
    private val config: PartymodeConfig  by inject()

    fun notify(message: String){

    }
}