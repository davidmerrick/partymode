package com.merricklabs.partymode

import com.merricklabs.partymode.testutil.TestConstants.INTEGRATION_GROUP
import org.koin.standalone.StandAloneContext.loadKoinModules
import org.koin.standalone.StandAloneContext.stopKoin
import org.koin.standalone.inject
import org.koin.test.KoinTest
import org.koin.test.declareMock
import org.mockito.Mockito
import org.testng.annotations.AfterMethod
import org.testng.annotations.BeforeMethod

open class PartymodeIntegrationTestBase : KoinTest {

    // Workaround for Mockito in Kotlin. See https://medium.com/@elye.project/befriending-kotlin-and-mockito-1c2e7b0ef791
    protected fun <T> any(): T {
        Mockito.any<T>()
        return uninitialized()
    }

    private fun <T> uninitialized(): T = null as T

    val partymodeConfig: PartymodeConfig by inject()

    @BeforeMethod(groups = [INTEGRATION_GROUP])
    fun beforeMethod() {
        loadKoinModules(PartymodeModule)
        declareMock<PartymodeConfig>()
    }

    @AfterMethod(groups = [INTEGRATION_GROUP])
    fun afterMethod() {
        stopKoin()
    }
}