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

package io.github.simonschiller.prefiller.testutil.spec

import java.io.File
import java.util.Properties

abstract class BaseProjectSpec : ProjectSpec {

    override fun getSettingsGradleContent(): String = "include(':module')"

    override fun getGradlePropertiesContent() = """
        org.gradle.jvmargs=-XX:MaxMetaspaceSize=2g
        android.useAndroidX=true
    """.trimIndent()

    override fun getLocalPropertiesContent() = "sdk.dir=${getAndroidHome()}"

    override fun getAndroidManifestContent() = """
        <manifest package="com.test" />
    """.trimIndent()

    override fun getScriptFileContent() = """
        INSERT INTO people(name, age) VALUES ("Mikael Burke", 38);
        INSERT INTO people(name, age) VALUES ("Ayana Clarke", 12);
        INSERT INTO people(name, age) VALUES ("Malachy Wall", 24);
    """.trimIndent()

    override fun generateAdditionalFiles(rootDir: File) {
        // By default, no additional files are generated
    }

    private fun getAndroidHome(): String {
        System.getenv("ANDROID_HOME")?.let { return it.normalizeLineSeparators() }

        val localProperties = File(System.getProperty("user.dir")).resolveSibling("local.properties")
        if (localProperties.exists()) {
            val properties = Properties()
            localProperties.inputStream().use { properties.load(it) }
            properties.getProperty("sdk.dir")?.let { return it.normalizeLineSeparators() }
        }
        error("Missing 'ANDROID_HOME' environment variable or local.properties with 'sdk.dir'")
    }

    private fun String.normalizeLineSeparators(): String {
        val nonUnixLineSeparators = Regex("\r\n|\r")
        return replace(nonUnixLineSeparators, "\n")
    }
}
