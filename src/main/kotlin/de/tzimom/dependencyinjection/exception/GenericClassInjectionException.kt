package de.tzimom.dependencyinjection.exception

import kotlin.reflect.KClass

data class GenericClassInjectionException(val targetClass: KClass<*>)
    : Exception("Cannot inject generic class ${targetClass.simpleName}")