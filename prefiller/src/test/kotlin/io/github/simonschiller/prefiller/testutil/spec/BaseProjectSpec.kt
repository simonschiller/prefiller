package io.github.simonschiller.prefiller.testutil.spec

import org.gradle.kotlin.dsl.support.normaliseLineSeparators
import java.io.File
import java.util.*

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
        System.getenv("ANDROID_HOME")?.let { return it.normaliseLineSeparators() }

        val localProperties = File(System.getProperty("user.dir")).resolveSibling("local.properties")
        if (localProperties.exists()) {
            val properties = Properties()
            localProperties.inputStream().use { properties.load(it) }
            properties.getProperty("sdk.dir")?.let { return it.normaliseLineSeparators() }
        }
        error("Missing 'ANDROID_HOME' environment variable or local.properties with 'sdk.dir'")
    }
}
