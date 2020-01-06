package nl.info.gradle.plugin

import org.gradle.api.tasks.*
import org.mule.client.codegen.CodeGenConfig
import org.mule.client.codegen.OutputVersion
import org.mule.client.codegen.RamlJavaClientGenerator
import java.io.File
import javax.inject.Inject

open class RamlJavaClientGeneratorTask
    @Inject constructor(config: RamlJavaClientGeneratorExtension)
    : SourceTask() {

    @Input
    @Optional
    var basePackage = "raml"

    @OutputDirectory
    @Optional
    var targetFolder = "build/generated-raml-client"

    @Input
    @Optional
    var includeAdditionalProperties = true

    @Input
    @Optional
    var targetVersion = "1.8"

    @Input
    @Optional
    var useBigDecimals = false

    @Input
    @Optional
    var useJava8Dates = true

    @Input
    @Optional
    var useJava8Optional = true

    @Input
    @Optional
    var outputVersion = "v1"

    @TaskAction
    fun compile() {
        val resource = source.files.iterator().next()?.toURI()?.toURL()

        logger.lifecycle("Starting java code generation"
                + "ramlResource = ${resource}"
                + "targetFolder = ${targetFolder}"
                + "basePackage  = ${basePackage}")

        val targetDir = File(targetFolder)
        if (!targetDir.isDirectory) {
            logger.info("create output dir: ${targetDir}")
            if (!targetDir.mkdirs())
                throw RuntimeException("Failed to create target dir: ${targetDir}")
        }

        // Code Generation Configuration
        val codeGenConfig = CodeGenConfig()
        codeGenConfig.includeAdditionalProperties = includeAdditionalProperties
        codeGenConfig.targetVersion = targetVersion
        codeGenConfig.useBigDecimals = useBigDecimals
        codeGenConfig.useJava8Dates = useJava8Dates
        codeGenConfig.useJava8Optional = useJava8Optional

        // Parse the output version
        val outV = OutputVersion.valueOf(outputVersion)

        logger.info("RamlJavaClientGenerator configuration:"
                + "outputVersion               = ${outputVersion}"
                + "targetVersion               = ${targetVersion}"
                + "includeAdditionalProperties = ${includeAdditionalProperties}"
                + "useBigDecimals              = ${useBigDecimals}"
                + "useJava8Dates               = ${useJava8Dates}"
                + "useJava8Optional            = ${useJava8Optional}")

        // Generate code from RAML file
        val javaClientGenerator = RamlJavaClientGenerator(basePackage, targetDir, outV, codeGenConfig)
        javaClientGenerator.generate(resource)
    }

    init {
        basePackage = config.basePackage
        targetFolder = config.targetFolder
        includeAdditionalProperties = config.includeAdditionalProperties
        targetVersion = config.targetVersion
        useBigDecimals = config.useBigDecimals
        useJava8Dates = config.useJava8Dates
        useJava8Optional = config.useJava8Optional
        outputVersion = config.outputVersion
    }
}