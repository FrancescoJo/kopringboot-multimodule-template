package com.github.francescojo.lib.lang

import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf

/**
 * @since 2025-09-08
 */
object ReflectionUtils {
    fun <T : Any> findAllConcreteImplementationsOf(sealedClass: KClass<T>): Set<KClass<out T>> {
        when {
            !sealedClass.isSealed -> {
                throw IllegalArgumentException(
                    "Class ${sealedClass.qualifiedName} is not a sealed class or interface. " +
                            "Only sealed classes and sealed interfaces are supported."
                )
            }

            sealedClass.isAbstract && sealedClass.sealedSubclasses.isEmpty() -> {
                throw IllegalArgumentException(
                    "Sealed class ${sealedClass.qualifiedName} has no subclasses. " +
                            "This might indicate the subclasses are not compiled or accessible."
                )
            }
        }

        return findAllConcreteImplementationsOfInternal(sealedClass)
    }

    private fun <T : Any> findAllConcreteImplementationsOfInternal(sealedClass: KClass<T>): Set<KClass<out T>> {
        val result = mutableSetOf<KClass<out T>>()
        val visited = mutableSetOf<KClass<*>>()

        fun traverse(current: KClass<out T>) {
            if (current in visited) {
                return
            }
            visited.add(current)

            val subclasses = current.sealedSubclasses
            if (subclasses.isEmpty()) {
                result.add(current)
            } else {
                subclasses.forEach { subclass ->
                    if (subclass.isSubclassOf(sealedClass)) {
                        traverse(subclass)
                    }
                }
            }
        }

        traverse(sealedClass)
        return result
    }
}
