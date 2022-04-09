package de.tzimom.dependencyinjection

import de.tzimom.dependencyinjection.annotation.Inject
import de.tzimom.dependencyinjection.exception.AmbiguousConstructorsException
import de.tzimom.dependencyinjection.exception.GenericClassInjectionException
import de.tzimom.dependencyinjection.exception.MissingDependencyException

import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.primaryConstructor

class Injector<T : Any>(private val targetClass: KClass<T>, private val targetConstructor: KFunction<T>) {
    init {
        if (targetClass.typeParameters.isNotEmpty()) throw GenericClassInjectionException(targetClass)
    }

    fun inject(arguments: List<Any>): T {
        val mappedParameters = mapParameters(arguments)
        return targetConstructor.callBy(mappedParameters)
    }

    private fun mapParameters(arguments: List<Any>) =
        targetConstructor.parameters.associateWith { matchArgument(arguments, it) }

    private fun matchArgument(arguments: List<Any>, dependency: KParameter): Any {
        val dependencyClass = dependency.type.classifier as KClass<*>

        return arguments.firstOrNull { it::class.isSubclassOf(dependencyClass) }
            ?: throw MissingDependencyException(targetClass, dependencyClass)
    }

    companion object {
        private fun <T : Any> findSuitableConstructor(targetClass: KClass<T>): KFunction<T> {
            val constructors = targetClass.constructors
            if (constructors.size == 1) return constructors.first()

            val annotatedConstructors = constructors.filter { it.hasAnnotation<Inject>() }
            if (annotatedConstructors.size == 1) return annotatedConstructors.first()

            return targetClass.primaryConstructor ?: throw AmbiguousConstructorsException(targetClass)
        }

        fun <T : Any> fromClass(targetClass: KClass<T>) = Injector(targetClass, findSuitableConstructor(targetClass))
        fun <T : Any> injectClass(targetClass: KClass<T>, arguments: List<Any>) = fromClass(targetClass).inject(arguments)

        inline fun <reified T : Any> injectClass(arguments: List<Any>) = injectClass(T::class, arguments)
        inline fun <reified T : Any> injectClass(vararg arguments: Any) = injectClass(T::class, arguments.asList())

        fun <T : Any> KClass<T>.inject(arguments: List<Any>) = injectClass(this, arguments)
        fun <T : Any> KClass<T>.inject(vararg arguments: Any) = inject(arguments.asList())
    }
}