plugins {
    id("com.android.library")
    id("kotlin-android")
    id("com.google.devtools.ksp")
    id("io.github.simonschiller.prefiller")
}

android {
    compileSdkVersion(Versions.COMPILE_SDK)

    defaultConfig {
        minSdkVersion(Versions.MIN_SDK)
        targetSdkVersion(Versions.TARGET_SDK)

        ksp {
            arg("room.schemaLocation", "$projectDir/schemas")
        }
    }
    sourceSets {
        named("main").configure {
            manifest.srcFile("$projectDir/../shared-src/AndroidManifest.xml")
            java.srcDirs("$projectDir/../shared-src/kotlin/main")
        }
        named("test").configure {
            java.srcDirs("$projectDir/../shared-src/kotlin/test")
        }
    }
    testOptions {
        unitTests.apply {
            isIncludeAndroidResources = true
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation(Dependencies.KOTLIN_STDLIB)
    implementation(Dependencies.APPCOMPAT)
    implementation(Dependencies.ROOM_RUNTIME)
    ksp(Dependencies.ROOM_COMPILER)

    testImplementation(Dependencies.JUNIT_4)
    testImplementation(Dependencies.ANDROIDX_TEST)
    testImplementation(Dependencies.ROBOLECTRIC)
}

prefiller {
    database("customers") {
        classname.set("io.github.simonschiller.prefiller.sample.customer.CustomerDatabase")
        script.set(layout.projectDirectory.file("../shared-src/sql/customers.sql"))
    }
    database("products") {
        classname.set("io.github.simonschiller.prefiller.sample.product.ProductDatabase")
        script.set(layout.projectDirectory.file("../shared-src/sql/products.sql"))
    }
}
