package io.github.davidmerrick.partymode.util

import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import io.micronaut.core.annotation.Introspected
import io.micronaut.jackson.JacksonConfiguration
import io.micronaut.jackson.ObjectMapperFactory
import javax.inject.Singleton

@Factory
@Introspected
@Replaces(ObjectMapperFactory::class)
class CustomObjectMapperFactory : ObjectMapperFactory() {

    @Singleton
    @Replaces(ObjectMapper::class)
    override fun objectMapper(jacksonConfiguration: JacksonConfiguration?, jsonFactory: JsonFactory?): ObjectMapper {
        return super.objectMapper(jacksonConfiguration, jsonFactory)
                .registerKotlinModule()
    }
}