object Versions {
    const val COMPILE_SDK = 29
    const val BUILD_TOOLS = "29.0.2"
    const val MIN_SDK = 21
    const val TARGET_SDK = 29

    const val AGP = "4.0.0"
    const val KOTLIN = "1.3.72"

    const val ANTLR = "4.8"
    const val GSON = "2.8.6"
    const val SQLITE = "3.31.1"
    const val JUNIT_5 = "5.6.2"

    const val APPCOMPAT = "1.1.0"
    const val ROOM = "2.2.5"
    const val JUNIT_4 = "4.13"
    const val ANDROIDX_TEST = "1.1.1"
    const val ROBOLECTRIC = "4.3.1"
}

object Dependencies {
    const val AGP = "com.android.tools.build:gradle:${Versions.AGP}"
    const val KOTLIN_GRADLE_PLUGIN = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.KOTLIN}"

    const val ANTLR = "org.antlr:antlr4:${Versions.ANTLR}"
    const val GSON = "com.google.code.gson:gson:${Versions.GSON}"
    const val SQLITE = "org.xerial:sqlite-jdbc:${Versions.SQLITE}"
    const val JUNIT_5_ENGINE = "org.junit.jupiter:junit-jupiter-engine:${Versions.JUNIT_5}"
    const val JUNIT_5_API = "org.junit.jupiter:junit-jupiter-api:${Versions.JUNIT_5}"
    const val JUNIT_5_PARAMS = "org.junit.jupiter:junit-jupiter-params:${Versions.JUNIT_5}"

    const val KOTLIN_STDLIB = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.KOTLIN}"
    const val APPCOMPAT = "androidx.appcompat:appcompat:${Versions.APPCOMPAT}"
    const val ROOM_RUNTIME = "androidx.room:room-runtime:${Versions.ROOM}"
    const val ROOM_COMPILER = "androidx.room:room-compiler:${Versions.ROOM}"
    const val JUNIT_4 = "junit:junit:${Versions.JUNIT_4}"
    const val ANDROIDX_TEST = "androidx.test.ext:junit:${Versions.ANDROIDX_TEST}"
    const val ROBOLECTRIC = "org.robolectric:robolectric:${Versions.ROBOLECTRIC}"
}
