package io.github.simonschiller.prefiller.testutil.compatibility

import io.github.simonschiller.prefiller.internal.util.Version

internal object RoomVersionCompatibility {
    fun getCompatibleAndroidxCoreRuntimeVersion(
        compileSdkVersion: Int,
    ): Version {
        return if (compileSdkVersion >= 33) {
            Version.parse(Versions.CORE_RUNTIME)
        } else {
            Version(2, 1, 0)
        }
    }

    fun getCompatibleRoomVersion(
        compileSdkVersion: Int,
        jvmVersion: Int = Runtime.version().version()[0],
    ): Version = when {
        compileSdkVersion >= 34 -> {
            if (jvmVersion >= 17) {
                // Room 2.6.0 requires JDK 17 (https://issuetracker.google.com/issues/311218683) and version 34
                // of the Android APIs
                Version.parse(Versions.ROOM)
            } else {
                Version(2, 5, 2)
            }
        }

        compileSdkVersion >= 33 -> Version(2, 5, 2)
        else -> Version(2, 4, 3)
    }
}
