package org.koin.android.architecture.ext.koin

import org.koin.core.KoinContext
import org.koin.core.bean.BeanDefinition
import org.koin.core.parameter.Parameters
import org.koin.error.NoBeanDefFoundException

/**
 * Retrieve an instance by its class canonical name
 */
fun <T> KoinContext.getByTypeName(canonicalName: String, parameters: Parameters): T {
    val foundDefinitions =
        beanRegistry.definitions.filter { it.clazz.java.canonicalName == canonicalName }.distinct()
    return getWithDefinitions(foundDefinitions, parameters, "for class name '$canonicalName'")
}

/**
 * Retrieve an instance by its bean beanDefinition name
 */
fun <T> KoinContext.getByName(name: String, parameters: Parameters): T {
    val foundDefinitions = beanRegistry.definitions.filter { it.name == name }.distinct()
    return getWithDefinitions(foundDefinitions, parameters, "for bean name '$name'")
}

/**
 * Retrieve bean beanDefinition instance from given definitions
 */
private fun <T> KoinContext.getWithDefinitions(
    foundDefinitions: List<BeanDefinition<*>>,
    parameters: Parameters,
    message: String
): T {
    return when (foundDefinitions.size) {
        0 -> throw NoBeanDefFoundException("No bean beanDefinition found $message")
        1 -> {
            val def = foundDefinitions.first()
            resolveInstance(def.clazz, parameters, { listOf(def) })
        }
        else -> throw NoBeanDefFoundException("Multiple bean definitions found $message")
    }
}