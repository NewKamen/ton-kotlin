@file:Suppress("OPT_IN_USAGE")

package org.ton.hashmap

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.tlb.TlbCodec
import org.ton.tlb.TlbCombinator
import kotlin.jvm.JvmStatic

@JsonClassDiscriminator("@type")
@Serializable
sealed interface AugDictionary<out X, out Y> : Iterable<Pair<X, Y>> {
    val extra: Y

    override fun iterator(): Iterator<Pair<X, Y>> = nodes().iterator()
    fun nodes(): Sequence<Pair<X, Y>>

    companion object {
        @Suppress("UNCHECKED_CAST")
        @JvmStatic
        fun <X, Y> tlbCodec(
            n: Int,
            x: TlbCodec<X>,
            y: TlbCodec<Y>
        ): TlbCombinator<AugDictionary<X, Y>> =
            AugDictionaryTlbCombinator(n, x, y) as TlbCombinator<AugDictionary<X, Y>>
    }
}

private class AugDictionaryTlbCombinator<X, Y>(
    n: Int,
    x: TlbCodec<X>,
    y: TlbCodec<Y>
) : TlbCombinator<AugDictionary<*, *>>(
    AugDictionary::class,
    AugDictionaryEmpty::class to AugDictionaryEmpty.tlbCodec<X, Y>(y),
    AugDictionaryRoot::class to AugDictionaryRoot.tlbCodec(n, x, y),
)
