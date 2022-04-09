package de.tzimom.dependencyinjection.exception

import kotlin.reflect.KClass

data class MissingDependencyException(val targetClass: KClass<*>, val dependencyClass: KClass<*>)
    : Exception("Cannot inject class ${targetClass.simpleName} due to lack of dependency for class ${dependencyClass.simpleName}")