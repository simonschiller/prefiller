package io.github.simonschiller.prefiller.integration

import io.github.simonschiller.prefiller.testutil.ProjectExtension
import org.junit.jupiter.api.extension.RegisterExtension

abstract class BaseIntegrationTest {

    @JvmField
    @RegisterExtension
    val project = ProjectExtension()
}
