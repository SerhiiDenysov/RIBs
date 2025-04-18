package com.badoo.ribs.store

import com.badoo.ribs.core.Rib
import kotlin.reflect.KClass

internal class RetainedInstanceStoreImpl : RetainedInstanceStore {

    private val map: MutableMap<Rib.Identifier, MutableMap<KClass<*>, ValueHolder<*>>> = HashMap()

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> get(
        owner: Rib.Identifier,
        clazz: KClass<*>,
        disposer: (T) -> Unit,
        factory: () -> T
    ): T =
        map
            .getOrPut(owner) { HashMap() }
            .getOrPut(clazz) { ValueHolder(factory(), disposer) }
            .value as T

    override fun isRetainedByOwner(owner: Rib.Identifier, value: Any): Boolean =
        map[owner]
            ?.let { valueHolderMap ->
                valueHolderMap.values.any { valueHolder ->
                    valueHolder.value === value
                }
            }
            ?: false

    override fun isRetained(value: Any): Boolean =
        map.values.any { valueHolderMap ->
            valueHolderMap.values.any { valueHolder ->
                valueHolder.value === value
            }
        }

    override fun removeAll(owner: Rib.Identifier) {
        map.remove(owner)?.values?.forEach { it.dispose() }
    }

    private class ValueHolder<T : Any>(
        val value: T,
        private val disposer: (T) -> Unit
    ) {
        fun dispose() {
            disposer(value)
        }
    }
}
