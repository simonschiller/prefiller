package io.github.simonschiller.prefiller.testutil.spec

interface ProjectSpec {
    fun getSettingsGradleContent(): String
    fun getGradlePropertiesContent(): String
    fun getLocalPropertiesContent(): String

    fun getRootBuildGradleContent(agpVersion: String): String
    fun getModuleBuildGradleContent(): String

    fun getAndroidManifestContent(): String
    fun getScriptFileContent(): String

    fun getEntityClassName(): String
    fun getEntityClassContent(): String
    fun getDatabaseClassName(): String
    fun getDatabaseClassContent(): String
}
