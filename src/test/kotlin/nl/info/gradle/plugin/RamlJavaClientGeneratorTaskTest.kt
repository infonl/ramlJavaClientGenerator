/*
 * This Kotlin source file was generated by the Gradle 'init' task.
 */
package nl.info.gradle.plugin

import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginConvention
import org.gradle.testfixtures.ProjectBuilder
import java.io.File
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

/**
 * A simple unit test for the RamlJavaClientGeneratorTask.
 */
class RamlJavaClientGeneratorTaskTest {

    @Test
    fun `task won't run without source`() {
        // Setup the RamlJavaClientGeneratorTask
        val task = initGenerateRamlJavaClientTask(initProject())
        task.source = ""

        val exception = assertFailsWith<RamlJavaClientGeneratorConfigurationException> {
            task.compile()
        }
        assertTrue(exception.message.equals("No source provided."))
    }

    @Test
    fun `task won't run if source is invalid`() {
        // Setup the RamlJavaClientGeneratorTask
        val task = initGenerateRamlJavaClientTask(initProject())
        task.source = "invalid.source"

        val exception = assertFailsWith<RamlJavaClientGeneratorConfigurationException> {
            task.compile()
        }
        assertHasExceptionMessage(exception, "Unable to find source file: invalid.source.")
    }

    @Test
    fun `task won't run without targetFolder`() {
        // Setup the RamlJavaClientGeneratorTask
        val task = initGenerateRamlJavaClientTask(initProject())
        task.targetFolder = ""

        val exception = assertFailsWith<RamlJavaClientGeneratorConfigurationException> {
            task.compile()
        }
        assertHasExceptionMessage(exception, "No targetFolder provided.")
    }

    @Test
    fun `task won't run using Java 8 Optional, if Java source version ain't compatible`() {
        // Setup the RamlJavaClientGeneratorTask
        val project = initProject()
        val generateRamlJavaClientTask = initGenerateRamlJavaClientTask(project)
        val javaPluginConvention: JavaPluginConvention = project.convention.getPlugin(JavaPluginConvention::class.java)
        // Java Source compatibity = 1.7
        javaPluginConvention.sourceCompatibility = JavaVersion.VERSION_1_7
        // Try to use Java 8 Optionals
        generateRamlJavaClientTask.useJava8Optional = true

        val exception = assertFailsWith<RamlJavaClientGeneratorConfigurationException> {
            generateRamlJavaClientTask.compile()
        }
        assertHasExceptionMessage(exception, "Java source compatibility, 1.7 does not support Java 8 optionals.")
    }

    @Test
    fun `task won't run using Java 8 Dates, if Java source version ain't compatible`() {
        // Setup the RamlJavaClientGeneratorTask
        val project = initProject()
        val generateRamlJavaClientTask = initGenerateRamlJavaClientTask(project)
        val javaPluginConvention: JavaPluginConvention = project.convention.getPlugin(JavaPluginConvention::class.java)
        // Java Source compatibity = 1.7
        javaPluginConvention.sourceCompatibility = JavaVersion.VERSION_1_7
        // Try to use Java 8 Optionals
        generateRamlJavaClientTask.useJava8Dates = true

        val exception = assertFailsWith<RamlJavaClientGeneratorConfigurationException> {
            generateRamlJavaClientTask.compile()
        }
        assertHasExceptionMessage(exception, "Java source compatibility, 1.7 does not support Java 8 dates.")
    }

    private fun assertHasExceptionMessage(exception: RamlJavaClientGeneratorConfigurationException, expectedMessage: String) {
        assertTrue(exception.message.equals(expectedMessage),
                "Exception message invalid. Expected: \"$expectedMessage\", actual: \"${exception.message}\"")
    }

    private fun initGenerateRamlJavaClientTask(project: Project): RamlJavaClientGeneratorTask {
        val thisClassFile = File(RamlJavaClientGeneratorTaskTest::class.java.getResource(".").path)
        var buildDir = thisClassFile
        while (!buildDir.name.equals("build")) {
            buildDir = buildDir.parentFile
        }
        val task = project.tasks.findByName(RamlJavaClientGeneratorPlugin.TASK_NAME)
        assertTrue(task is RamlJavaClientGeneratorTask)
        task.source = "${buildDir.absolutePath}/resources/test/empty-test-file.raml"
        return task
    }

    private fun initProject(): Project {
        val project = ProjectBuilder.builder().build()
        project.plugins.apply("java")
        project.plugins.apply(requireNotNull(RamlJavaClientGeneratorPlugin::class.qualifiedName))
        return project
    }
}
