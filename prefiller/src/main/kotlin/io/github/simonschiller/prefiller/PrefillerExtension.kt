package io.github.simonschiller.prefiller

import groovy.lang.Closure
import org.gradle.api.model.ObjectFactory
import org.gradle.util.ConfigureUtil

open class PrefillerExtension(private val objects: ObjectFactory) {
    internal val databaseConfigs = mutableListOf<DatabaseConfig>()

    // For configuration from Groovy DSL build files
    fun database(name: String, config: Closure<Unit>) {
        val databaseConfig = DatabaseConfig(name, objects)
        ConfigureUtil.configure(config, databaseConfig)
        addConfig(databaseConfig)
    }

    // For configuration from Kotlin DSL build files
    fun database(name: String, config: DatabaseConfig.() -> Unit) {
        val databaseConfig = DatabaseConfig(name, objects).apply(config)
        addConfig(databaseConfig)
    }

    private fun addConfig(config: DatabaseConfig) {
        if (databaseConfigs.any { it.name == config.name }) {
            throw IllegalStateException("There is already a database named ${config.name}")
        }
        databaseConfigs += config
    }
}
