package io.github.simonschiller.prefiller

import org.gradle.api.Action
import org.gradle.api.model.ObjectFactory

open class PrefillerExtension(private val objects: ObjectFactory) {
    internal val databaseConfigs = mutableListOf<DatabaseConfig>()

    fun database(name: String, config: Action<DatabaseConfig>) {
        val databaseConfig = DatabaseConfig(name, objects)
        config.execute(databaseConfig)
        addConfig(databaseConfig)
    }

    private fun addConfig(config: DatabaseConfig) {
        if (databaseConfigs.any { it.name == config.name }) {
            throw IllegalStateException("There is already a database named ${config.name}")
        }
        databaseConfigs += config
    }
}
