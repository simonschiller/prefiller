plugins {
    id("com.android.library")
    id("io.github.simonschiller.prefiller")
}

android {
    compileSdkVersion(29)

    defaultConfig {
        minSdkVersion(21)
        targetSdkVersion(29)
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
    implementation("androidx.appcompat:appcompat:${properties["versions.appcompat"]}")
    implementation("androidx.room:room-runtime:${properties["versions.room"]}")
    annotationProcessor("androidx.room:room-compiler:${properties["versions.room"]}")

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
