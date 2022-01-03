object Versions { // See https://mvnrepository.com
    const val COMPILE_SDK = 31
    const val MIN_SDK = 21
    const val TARGET_SDK = 31

    const val AGP = "7.0.1"
    const val KOTLIN = "1.6.10"
    const val KSP = "1.6.10-1.0.2"

    const val ANTLR = "4.9.3"
    const val GSON = "2.8.9"
    const val SQLITE = "3.36.0.3"
    const val JUNIT_5 = "5.8.2"

    const val APPCOMPAT = "1.4.0"
    const val ROOM = "2.4.0"
    const val JUNIT_4 = "4.13.2"
    const val ANDROIDX_TEST = "1.1.4-alpha03" // Alpha version required to be compatible with Android 12
    const val ROBOLECTRIC = "4.7.3"
}

object Dependencies {
    const val AGP = "com.android.tools.build:gradle:${Versions.AGP}"
    const val KOTLIN_GRADLE_PLUGIN = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.KOTLIN}"
    const val KSP_GRADLE_PLUGIN = "com.google.devtools.ksp:symbol-processing-gradle-plugin:${Versions.KSP}"

    const val KOTLIN_STDLIB = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.KOTLIN}"
    const val ANTLR = "org.antlr:antlr4:${Versions.ANTLR}"
    const val GSON = "com.google.code.gson:gson:${Versions.GSON}"
    const val SQLITE = "org.xerial:sqlite-jdbc:${Versions.SQLITE}"
    const val JUNIT_5_ENGINE = "org.junit.jupiter:junit-jupiter-engine:${Versions.JUNIT_5}"
    const val JUNIT_5_API = "org.junit.jupiter:junit-jupiter-api:${Versions.JUNIT_5}"
    const val JUNIT_5_PARAMS = "org.junit.jupiter:junit-jupiter-params:${Versions.JUNIT_5}"

    const val APPCOMPAT = "androidx.appcompat:appcompat:${Versions.APPCOMPAT}"
    const val ROOM_RUNTIME = "androidx.room:room-runtime:${Versions.ROOM}"
    const val ROOM_COMPILER = "androidx.room:room-compiler:${Versions.ROOM}"
    const val JUNIT_4 = "junit:junit:${Versions.JUNIT_4}"
    const val ANDROIDX_TEST = "androidx.test.ext:junit:${Versions.ANDROIDX_TEST}"
    const val ROBOLECTRIC = "org.robolectric:robolectric:${Versions.ROBOLECTRIC}"
}
