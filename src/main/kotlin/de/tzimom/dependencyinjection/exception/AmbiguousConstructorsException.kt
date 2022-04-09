package de.tzimom.dependencyinjection.exception

import kotlin.reflect.KClass

data class AmbiguousConstructorsException(val targetClass: KClass<*>)
    : Exception("Cannot inject class ${targetClass.simpleName} due to its ambiguous injectable constructors")