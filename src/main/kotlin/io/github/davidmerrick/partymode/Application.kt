package io.github.davidmerrick.partymode

import io.micronaut.core.annotation.Introspected
import io.micronaut.runtime.Micronaut

@Introspected
object Application {

    @JvmStatic
    fun main(args: Array<String>) {
        Micronaut.build()
                .packages("io.github.davidmerrick.partymode")
                .mainClass(Application.javaClass)
                .start()
    }
}