package io.github.davidmerrick.partymode.testutil

import io.github.davidmerrick.partymode.storage.PartymodeStorage
import io.mockk.every
import javax.inject.Singleton

@Singleton
class TestHelpers(private val storage: PartymodeStorage) {
    fun enablePartymode() = setPartymode(true)
    fun disablePartymode() = setPartymode(false)
    private fun setPartymode(value: Boolean) = every {
        storage.isPartymodeEnabled()
    } returns value
}