package nl.info.gradle.plugin

import org.gradle.api.DefaultTask
import org.gradle.api.plugins.JavaPluginConvention
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.options.Option
import org.mule.client.codegen.OutputVersion
import org.mule.client.codegen.RamlJavaClientGenerator
import java.io.File
import java.net.URL
import javax.inject.Inject

open class RamlJavaClientGeneratorTask
    @Inject constructor(config: RamlJavaClientGeneratorExtension)
    : DefaultTask() {

    private val generatorConfig: RamlJavaClientGeneratorExtension = config

    @Input
    var source = config.source

    @Input
    @Option(option = "basePackage", description = "Configures the URL to be write to the output.")
    @Optional
    var basePackage = config.basePackage

    @OutputDirectory
    @Optional
    var targetFolder = config.targetFolder

    @Input
    var includeAdditionalProperties = config.includeAdditionalProperties

    @Input
    var useBigDecimals = config.useBigDecimals

    @Input
    var useJava8Dates = config.useJava8Dates

    @Input
    var useJava8Optional = config.useJava8Optional

    @Input
    @Optional
    var outputVersion: OutputVersion = config.outputVersion

    @TaskAction
    fun compile() {
        if (source.isBlank()) {
            throw RamlJavaClientGeneratorConfigurationException("No source provided.")
        }

        val resource: URL? =
                if (source.startsWith("http://") || source.startsWith("https://")) {
                    URL(source)
                } else {
                    val sourceFile = project.file(source)
                    if (!sourceFile.exists()) {
                        throw RamlJavaClientGeneratorConfigurationException("Unable to find source file: $source.")
                    }
                    sourceFile.toURI().toURL()
                }

        logger.lifecycle("""Starting java code generation
            |source       = $resource
            |targetFolder = $targetFolder
            |basePackage  = $basePackage""".trimMargin())

        if (targetFolder.isBlank()) {
            throw RamlJavaClientGeneratorConfigurationException("No targetFolder provided.")
        }
        val targetDir = File(targetFolder)
        if (!targetDir.isDirectory) {
            logger.info("create output dir: $targetDir")
            if (!targetDir.mkdirs())
                throw RamlJavaClientGeneratorConfigurationException("Unable to setup the target folder: $targetFolder")
        }

        val javaPluginConvention: JavaPluginConvention = project.convention.getPlugin(JavaPluginConvention::class.java)
        val targetVersion = javaPluginConvention.sourceCompatibility.name

        if (!javaPluginConvention.sourceCompatibility.isJava8Compatible) {
            if (useJava8Dates) {
                throw RamlJavaClientGeneratorConfigurationException("Java source compatibility, " +
                        "${javaPluginConvention.sourceCompatibility} does not support Java 8 dates.")
            }
            if (useJava8Optional) {
                throw RamlJavaClientGeneratorConfigurationException("Java source compatibility, " +
                        "${javaPluginConvention.sourceCompatibility} does not support Java 8 optionals.")
            }
        }

        logger.info("""RamlJavaClientGenerator configuration:
            |outputVersion               = $outputVersion
            |targetVersion               = $targetVersion
            |includeAdditionalProperties = $includeAdditionalProperties
            |useBigDecimals              = $useBigDecimals
            |useJava8Dates               = ${useJava8Dates}
            |useJava8Optional            = $useJava8Optional""".trimMargin())

        // Generate code from RAML file
        try {
            val javaClientGenerator = RamlJavaClientGenerator(basePackage, targetDir, outputVersion, generatorConfig)
            javaClientGenerator.generate(resource)
        } catch (exception: Throwable) {
            throw RamlJavaClientGeneratorProcessingException("Failed to generate java client code.", exception)
        }

        // Add output target as Java Compile task source
        val fileCollection = project.files(targetFolder)
        logger.debug("FileCollection = ${fileCollection.files}")

        val sourceSet = javaPluginConvention.sourceSets.maybeCreate("main")
        val updatedSourceSet = sourceSet.java.srcDirTrees.plus(fileCollection)
        sourceSet.java.setSrcDirs(updatedSourceSet)
        logger.debug("SourceSet - main = ${sourceSet.allJava.srcDirTrees}")


        if (javaPluginConvention.sourceCompatibility.isJava8Compatible) {
            // need to add javax.ws.rs-api dependency, if not already included
            project.dependencies.add("compile", "javax.ws.rs:javax.ws.rs-api:2.0.1")
        }
    }
}