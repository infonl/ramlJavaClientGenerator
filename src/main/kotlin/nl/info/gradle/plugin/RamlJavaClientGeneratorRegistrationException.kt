package nl.info.gradle.plugin

import org.gradle.api.GradleException

class RamlJavaClientGeneratorRegistrationException(message: String) : GradleException(message)

class RamlJavaClientGeneratorConfigurationException(message: String) : GradleException(message)

class RamlJavaClientGeneratorProcessingException(message: String, cause: Throwable?) : GradleException(message, cause)