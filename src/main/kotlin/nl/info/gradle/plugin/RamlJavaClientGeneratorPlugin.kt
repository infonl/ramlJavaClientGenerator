package nl.info.gradle.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin

class RamlJavaClientGeneratorPlugin : Plugin<Project> {
    companion object {
        const val TASK_NAME = "generateRamlJavaClient"
    }

    override fun apply(project: Project) {
        if (!project.plugins.hasPlugin("java")) {
            throw RamlJavaClientGeneratorRegistrationException("Task $TASK_NAME requires java plugin")
        }

        // Register the extension
        val extension = project.extensions.create("GenerateRamlJavaClient", RamlJavaClientGeneratorExtension::class.java)
        // Register the task
        val allTasks = project.tasks
        val task = allTasks.register(TASK_NAME, RamlJavaClientGeneratorTask::class.java, extension)
        // Order the task before compileJava
        val javaCompileTask = allTasks.findByName(JavaPlugin.COMPILE_JAVA_TASK_NAME)
        javaCompileTask?.dependsOn?.add(task)
    }
}
