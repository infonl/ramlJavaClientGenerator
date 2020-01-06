package nl.info.gradle.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

class RamlJavaClientGeneratorPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        // Register the extension
        val extension = project.extensions.create("generateRamlJavaClient", RamlJavaClientGeneratorExtension::class.java)
        // Register the task
        project.getTasks().create("generateramljavaclient", RamlJavaClientGeneratorTask::class.java, extension )
    }
}
