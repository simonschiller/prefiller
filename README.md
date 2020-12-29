[![Build Status](https://img.shields.io/travis/com/simonschiller/prefiller)](https://travis-ci.com/github/simonschiller/prefiller)
[![GitHub Release](https://img.shields.io/github/v/release/simonschiller/prefiller)](https://github.com/simonschiller/prefiller/releases)
[![License](https://img.shields.io/github/license/simonschiller/prefiller)](https://github.com/simonschiller/prefiller/blob/master/LICENSE)

# Prefiller

Prefiller is a Gradle plugin that generates pre-filled Room databases at compile time. 

## Motivation

With version 2.2, the Room persistence library added [support for pre-populated databases](https://medium.com/androiddevelopers/packing-the-room-pre-populate-your-database-with-this-one-method-333ae190e680). This works by including a pre-filled database in the assets directory. Generating this database can be tedious and error-prone and has to be repeated whenever the underlying schema changes.

Prefiller offers a convenient way to generate pre-filled databases at compile time. You simply provide a script file that populates your database, Prefiller takes care of the rest. It will generate a database matching the latest schema, execute your script on this database and include the database in the assets. This way, changing the schema or adding additional pre-filled data is much easier. Additionally, the changes to the pre-filled database are now present in script form, making changes easier to review in pull requests.

## Usage

To start using Prefiller, you just have to follow these steps.

#### Add Prefiller to your project

First you need to add the Prefiller plugin to your project by adding this block of code to your `build.gradle`.

```groovy
plugins {
    id "io.github.simonschiller.prefiller" version "0.1.0"
}
```

Alternatively, you can also use the legacy plugin API. Simply add the following snippet to your top-level `build.gradle`.

```groovy
buildscript {
    repositories {
        maven {
            url "https://plugins.gradle.org/m2/"
        }
    }
    dependencies {
        classpath "io.github.simonschiller:prefiller:0.1.0"
    }
}
```

When you're using the legacy plugin API, you also have to apply the plugin in the `build.gradle` of your module.

```groovy
apply plugin: "io.github.simonschiller.prefiller"
```

You can also find instructions on how to use the Prefiller plugin on the [Gradle plugin portal](https://plugins.gradle.org/plugin/io.github.simonschiller.prefiller).

#### Write your setup script

Next you need to create a `.sql` script with all your setup statements. Simply place this file somewhere in your project. Prefiller will use this file to populate the database, so make sure the statements are valid and match the database schema.

```sql
-- src/main/sql/setup.sql

INSERT INTO people(firstname, lastname, age) VALUES ("Mikael", "Burke", 38);
INSERT INTO people(firstname, lastname, age) VALUES ("Ayana", "Clarke", 12);
INSERT INTO people(firstname, lastname, age) VALUES ("Malachy", "Wall", 24);
```

#### Configure the Prefiller plugin

Lastly, you have to configure the Prefiller plugin in your `build.gradle` by linking the database class with the script file you just created.

```groovy
prefiller {
    database("people") {
        classname = "com.example.PeopleDatabase"
        script = file("$projectDir/src/main/sql/setup.sql")
    }
}
```

#### Using the pre-filled database in code

Now you're ready to go. Simply use the generated database file when you build your Room database.

```kotlin
val database = Room.databaseBuilder(context, com.example.PeopleDatabase::class.java, "people.db")
    .createFromAsset("people.db") // File name is configured in the plugin
    .build()
```

## How it works

Room can be set up to generate a schema definition file, this file contains all information needed to construct a matching database. Prefiller simply parses this file, generates the database accordingly and runs the provided script file. To make this work, you have to make sure that Room is configured to generate schema files.

```groovy
android {
    defaultConfig {
        // Java
        javaCompileOptions {
            annotationProcessorOptions {
                arguments["room.schemaLocation"] = "$projectDir/schemas".toString()
            }
        }

        // Kotlin
        kapt {
            arguments {
                arg("room.schemaLocation", "$projectDir/schemas")
            }
        }
    }
}
```

You can find more information on how to do this in the [official Room documentation](https://developer.android.com/training/data-storage/room/migrating-db-versions#export-schema).

## Working with this project

The source code of the plugin is located in the `prefiller` folder. The `sample` folder contains several sample projects that show how the plugin is used. The tests of these sample projects also function as E2E tests for the Prefiller plugin.

* Build the plugin: `./gradlew :prefiller:assemble`
* Run unit tests: `./gradlew :prefiller:test`
* Run all tests: `./gradlew test`

## License

```
Copyright 2020 Simon Schiller

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
