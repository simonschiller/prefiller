plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
    id("io.github.simonschiller.prefiller")
}

android {
    compileSdkVersion(Versions.COMPILE_SDK)

    defaultConfig {
        minSdkVersion(Versions.MIN_SDK)
        targetSdkVersion(Versions.TARGET_SDK)
        versionCode = 1
        versionName = "1.0"

        kapt {
            arguments {
                arg("room.schemaLocation", "$projectDir/schemas")
            }
        }
    }

    testOptions {
        unitTests.apply {
            isIncludeAndroidResources = true
        }
    }
}

dependencies {
    implementation(Dependencies.KOTLIN_STDLIB)
    implementation(Dependencies.APPCOMPAT)
    implementation(Dependencies.ROOM_RUNTIME)
    kapt(Dependencies.ROOM_COMPILER)

    testImplementation(Dependencies.JUNIT_4)
    testImplementation(Dependencies.ANDROIDX_TEST)
    testImplementation(Dependencies.ROBOLECTRIC)
}

prefiller {
    database("customers") {
        classname.set("io.github.simonschiller.prefiller.sample.customer.CustomerDatabase")
        script.set(layout.projectDirectory.file("src/main/sql/customers.sql"))
    }

    database("products") {
        classname.set("io.github.simonschiller.prefiller.sample.product.ProductDatabase")
        script.set(layout.projectDirectory.file("src/main/sql/products.sql"))
    }
}
