package nl.info.gradle.plugin

open class RamlJavaClientGeneratorExtension {
    var basePackage = "raml"
    var targetFolder = "build/generated-raml-client"
    var includeAdditionalProperties = true
    var targetVersion = "1.8"
    var useBigDecimals = false
    var useJava8Dates = true
    var useJava8Optional = true
    var outputVersion = "v1"
}
