package de.tzimom.dependencyinjection

import de.tzimom.dependencyinjection.Injector.Companion.inject

import kotlin.reflect.KClass

class DependencyCollection(vararg dependencies: Any) {
    private val dependencies = dependencies.toMutableList()

    fun addDependencies(vararg dependencies: Any) { this.dependencies.addAll(dependencies) }

    fun <T : Any> inject(targetClass: KClass<T>) = targetClass.inject(dependencies)
    inline fun <reified T : Any> inject() = inject(T::class)
}