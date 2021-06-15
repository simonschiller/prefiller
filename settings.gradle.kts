rootProject.name = "prefiller-plugin"

include(":prefiller")

// Samples can be excluded to publish the plugin when the API changes
if (!startParameter.projectProperties.containsKey("excludeSample")) {
    include(":sample:java")
    include(":sample:kotlin-kapt")
    include(":sample:kotlin-ksp")
}
