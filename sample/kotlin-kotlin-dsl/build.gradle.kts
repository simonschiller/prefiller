plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
    id("io.github.simonschiller.prefiller")
}

android {
    compileSdkVersion(properties["versions.compileSdk"].toString().toInt())

    defaultConfig {
        buildToolsVersion = properties["versions.buildTools"].toString()
        minSdkVersion(properties["versions.minSdk"].toString().toInt())
        targetSdkVersion(properties["versions.targetSdk"].toString().toInt())
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
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${properties["versions.kotlin"]}")
    implementation("androidx.appcompat:appcompat:${properties["versions.appcompat"]}")
    implementation("androidx.room:room-runtime:${properties["versions.room"]}")
    kapt("androidx.room:room-compiler:${properties["versions.room"]}")

    testImplementation("junit:junit:${properties["versions.junit4"]}")
    testImplementation("androidx.test.ext:junit:${properties["versions.androidxtest"]}")
    testImplementation("org.robolectric:robolectric:${properties["versions.robolectric"]}")
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
