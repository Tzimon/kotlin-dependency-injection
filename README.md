<h1 align="center">Kotlin Dependency Injection</h1>

> A small library for dependency injection in kotlin

## 🚀 Usage

The [`Injector`](./src/main/kotlin/de/tzimom/dependencyinjection/Injector.kt) class provides all the main functionality of the library.

You can inject a class in multiple ways:

- `Injector.injectClass(classReference, arguments)`
- `Injector.injectClass<ClassName>(arguments)`
- `classReference.inject(arguments)`

You can also use the [`DependencyCollection`](./src/main/kotlin/de/tzimom/dependencyinjection/DependencyCollection.kt) class to store multiple objects that will be injected:

```kotlin
val collection = DependencyCollection(someDependency)

collection.addDependencies(someOtherDependency)

val injectedObject = collection.inject(targetClass)
```

`DependencyCollection` acts as a sort of builder class meaning that you can chain the calls like this:

```kotlin
val injectedObject = DependencyCollection(someDependency)
    .addDependencies(someOtherDependency)
    .inject(targetClass)
```