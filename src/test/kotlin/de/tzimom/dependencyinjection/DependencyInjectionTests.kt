package de.tzimom.dependencyinjection

import de.tzimom.dependencyinjection.annotation.Inject
import de.tzimom.dependencyinjection.exception.AmbiguousConstructorsException
import de.tzimom.dependencyinjection.exception.GenericClassInjectionException
import de.tzimom.dependencyinjection.exception.MissingDependencyException

import org.junit.Test

import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertIs

@Suppress("UNUSED_PARAMETER")
class DependencyInjectionTests {
    @Test
    fun testInject() {
        data class InjectedClass(val testValue: Int)

        val injected = Injector.injectClass<InjectedClass>(5)

        assertEquals(injected, InjectedClass(5))
    }

    @Test
    fun testInjectMultipleConstructors() {
        data class InjectedClass(val testValue: Int) {
            constructor(): this(10)
        }

        val injected = Injector.injectClass<InjectedClass>(5)

        assertEquals(injected, InjectedClass(5))
    }

    @Test
    fun testInjectMultipleConstructorsOverride() {
        data class InjectedClass(val testValue: Int) {
            @Inject constructor(): this(10)
        }

        val injected = Injector.injectClass<InjectedClass>(5)

        assertEquals(injected, InjectedClass())
    }

    @Test
    fun testInjectMultipleDependencies() {
        data class InjectedClass(val testValue1: Int, val testValue2: String)

        val injected = Injector.injectClass<InjectedClass>("Hello", 5)

        assertEquals(injected, InjectedClass(5, "Hello"))
    }

    @Test
    fun testInjectFailsAmbiguousConstructors() {
        class InjectedClass {
            @Inject constructor(testValue: Int)
            @Inject constructor()
        }

        val exception = assertFails { Injector.injectClass<InjectedClass>(5) }

        assertIs<AmbiguousConstructorsException>(exception)
    }

    @Test
    fun testInjectFailsGenericClass() {
        data class InjectedClass<T>(val testValue: T)

        val exception = assertFails { Injector.injectClass<InjectedClass<Int>>(5) }

        assertIs<GenericClassInjectionException>(exception)
    }

    @Test
    fun testInjectFailsMissingDependency() {
        data class InjectedClass(val testValue: Int)

        val exception = assertFails { Injector.injectClass<InjectedClass>() }

        assertIs<MissingDependencyException>(exception)
    }
}