package nl.info.gradle.plugin

import org.mule.client.codegen.CodeGenConfig
import org.mule.client.codegen.OutputVersion

open class RamlJavaClientGeneratorExtension : CodeGenConfig() {
    var source = ""
    var basePackage = "raml"
    var targetFolder = "build/generated-raml-client"
    var includeAdditionalProperties = true
    var outputVersion: OutputVersion = OutputVersion.v1
}
