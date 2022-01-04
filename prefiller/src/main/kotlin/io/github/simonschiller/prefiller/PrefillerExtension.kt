/*
 * Copyright 2020 Simon Schiller
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
