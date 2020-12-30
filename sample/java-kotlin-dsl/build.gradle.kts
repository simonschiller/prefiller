plugins {
    id("com.android.library")
    id("io.github.simonschiller.prefiller")
}

android {
    compileSdkVersion(Versions.COMPILE_SDK)

    defaultConfig {
        buildToolsVersion = Versions.BUILD_TOOLS
        minSdkVersion(Versions.MIN_SDK)
        targetSdkVersion(Versions.TARGET_SDK)
        versionCode = 1
        versionName = "1.0"

        javaCompileOptions {
            annotationProcessorOptions {
                arguments["room.schemaLocation"] = "$projectDir/schemas"
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
    implementation(Dependencies.APPCOMPAT)
    implementation(Dependencies.ROOM_RUNTIME)
    annotationProcessor(Dependencies.ROOM_COMPILER)

    testImplementation(Dependencies.JUNIT_4)
    testImplementation(Dependencies.ANDROIDX_TEST)
    testImplementation(Dependencies.ROBOLECTRIC)
}

prefiller {
    database("customers") {
        classname = "io.github.simonschiller.prefiller.sample.customer.CustomerDatabase"
        script = file("$projectDir/src/main/sql/customers.sql")
    }

    database("products") {
        classname = "io.github.simonschiller.prefiller.sample.product.ProductDatabase"
        script = file("$projectDir/src/main/sql/products.sql")
    }
}
