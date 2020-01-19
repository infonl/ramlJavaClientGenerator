# Gradle Raml Java Client Generator
This [Gradle](https://docs.gradle.org/current/userguide/userguide.html) plugin can generate java rest client clode for a raml based api using a resource api approach. 
Supports both 0.8 and 1.0 versions of Raml.

## Mulesoft Labs RAML Client Generator
This gradle plugin wraps the [MuleSoft RAML Client Generator](https://github.com/mulesoft-labs/raml-java-client-generator), 
and exposes it's capabilities via a Gradle Plugin.

## Versioning
The versioning of this plugin will follow the MuleSoft RAML Client Generator version.

Current RAML Client Generator version included: 0.11-release.

This current version: 0.1.0.11

# Sample usage

## Minimal configuration 
```groovy
plugins {
    id('nl.info.gradle.plugin.RamlJavaClientGeneratorPlugin')
}

generateramljavaclient {
    source = "resources/simple_example.raml"
}        
```

# Configuration details
The configurations 

## Default full configuration
```groovy
generateramljavaclient {
    source = ""
    basePackage = "raml"
    targetFolder = "build/generated-raml-client"
    includeAdditionalProperties = true
    targetVersion = <copied from Java plugin source version>
    useBigDecimals = false
    useJava8Dates = true
    useJava8Optional = true
    outputVersion = "v1"
}        
```

### source
Source is either a remote or local RAML source file, either a URL or (relative) path.

### basePackage
Base package for the generated Java classes.

### targetFolder
The target location where the Java files will be generated into.

### includeAdditionalProperties
Additional properties that will be generated.

### targetVersion
_Unused!_ :
This property is available, but will the Java source version set in the project will be used instead.

### useBigDecimals
Use Big Decimals for numbers?

### useJava8Dates
Use Java 8 Dates?

### useJava8Optional
Use Java 8 Optionals?

### outputVersion
Raml generator output version; v1 or v2.

## Further details
Please refer to the MuleSoft documentation for the [RAML Java Client Generator](https://github.com/mulesoft-labs/raml-java-client-generator).
